package com.windy.restart;

import com.windy.itemreaderfile.Customer;
import org.omg.SendingContext.RunTime;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

/**
 * @description: 读取过程中出现异常恢复操作
 * @time: 2020-04-13 22:39
 * @author: baojinlong
 **/
@Component("restartReader")
public class RestartReader implements ItemStreamReader<Customer> {
    private FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
    private Long curLine = 0L;
    private boolean restart = false;
    private ExecutionContext executionContext;

    public RestartReader() {
        // 设置文件位置
        flatFileItemReader.setResource(new ClassPathResource("restart.txt"));
        // 跳过第一行
        flatFileItemReader.setLinesToSkip(1);
        // 从第二行进行数据解析
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("id", "firstName", "lastName", "birthday");
        // 将解析出来的数据映射为一个对象
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(delimitedLineTokenizer);
        mapper.setFieldSetMapper(fieldSet -> {
            // 创建最终对象
            Customer customer = new Customer();
            customer.setId(fieldSet.readInt("id"));
            customer.setFirstName(fieldSet.readString("firstName"));
            customer.setLastName(fieldSet.readString("lastName"));
            customer.setBirthday(fieldSet.readString("birthday"));
            return customer;
        });
        // 检查下结果集
        mapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(mapper);
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        this.curLine++;
        if (restart) {
            // 直接跳到多少行
            flatFileItemReader.setLinesToSkip(this.curLine.intValue() - 1);
            restart = false;
            System.out.println("start reading from line " + this.curLine + 1);
        }
        flatFileItemReader.open(this.executionContext);
        Customer customer = flatFileItemReader.read();
        if (customer != null && "wrongName".equals(customer.getFirstName())) {
            throw new RuntimeException("Something wrong.Customer is " + customer.getId());
        }
        return customer;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        if (this.executionContext.containsKey("curLine")) {
            this.curLine = this.executionContext.getLong("curLine");
            this.restart = true;
        } else {
            this.curLine = 0L;
            this.executionContext.put("curLine", this.curLine);
            System.out.println("Start reading from line " + this.curLine + 1);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // 根据chunk_size每读完10个数据就更新操作
        this.executionContext.put("curLine", this.curLine);
        System.out.println("currentLine=" + this.curLine);
    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("RestartReader.close");
    }
}
