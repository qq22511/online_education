package com.atguigu.eduservice.excel;


import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelTest {

    //循环设置要添加的数据，最终封装到list集合中
    private static List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);
        }
        return list;
    }


    @Test
    public  void test() throws Exception{
        String  filename = "D:\\Program Files\\feiq\\Recv Files\\day06.xlsx";
        EasyExcel.write(filename,DemoData.class).sheet("写入方法").doWrite(data());
    }

    @Test
    public  void  read (){
        String  filename = "D:\\Program Files\\feiq\\Recv Files\\day06.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }
}
