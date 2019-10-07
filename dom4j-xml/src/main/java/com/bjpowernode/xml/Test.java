package com.bjpowernode.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

/**
 * ClassName:Test
 * Package:com.bjpowernode.xml
 * Description:TODO
 *
 * @date:2019/9/3 11:01
 * @author:guoxin
 */
public class Test {

    public static void main(String[] args) throws DocumentException {
        String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><returnstatus>Success</returnstatus><message>ok</message><remainpoint>-1146645</remainpoint><taskID>103938075</taskID><successCounts>1</successCounts></returnsms>";

        //1.将xml格式的字符串转换为Document对象
        Document document = DocumentHelper.parseText(xmlString);

        //获取returnstatus节点的文本内容
        //获取returnstatus节点的路径表达式：/returnsms/returnstatus    或  //returnstatus
        Node node = document.selectSingleNode("/returnsms/returnstatus");

        //获取returnstatus节点的文本内容
        String text = node.getText();

        System.out.println("returnstatus的值：" + text);
    }
}
