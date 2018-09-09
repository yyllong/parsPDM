package parsePDm;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParsePDM {
	 	private Element root;
	    private String filename="销售系统数据库设计";
	    public ParsePDM() throws DocumentException{
	        SAXReader reader = new SAXReader();
	        File pdm = new File("D:\\"+filename+".pdm");
	        Document doc = reader.read(pdm);
	        root = doc.getRootElement();
	    }
		   
    /**
     * 加载所有domains
     * @param args
     */
    public List<Domain> loadAllDomains(){
        List<Domain> allDomains = new ArrayList<Domain>();
        Element domainNode = (Element) root.selectSingleNode("//c:Domains");
        for(Element e:domainNode.elements()){
            Domain domain = new Domain();
            String domainId = e.attributeValue("Id");
            String domainName = e.elementText("Name");
            domain.setDomainId(domainId);
            domain.setDomainName(domainName);
            allDomains.add(domain);
        }

        return allDomains;
    }
    /**
     * 加载所有references,外键关系
     * @param args
     */
    public List<Reference> loadAllReferences(){
        List<Reference> allReferences = new ArrayList<Reference>();
        Element refNode = (Element) root.selectSingleNode("//c:References");
        for(Element e:refNode.elements()){
            Reference ref = new Reference();
            String parentId = e.element("ParentTable").element("Table").attributeValue("Ref");
            String FKId = e.element("Joins").element("ReferenceJoin").element("Object2").element("Column").attributeValue("Ref");
            ref.setFKId(FKId);
            ref.setParentId(parentId);
            allReferences.add(ref);
        }
        return allReferences;
    }
    /**
     * 加载所有主键Id
     * @param args
     */
    public List<String> loadAllPKIds(){
        List<String> allPKIds = new ArrayList<String>();
        Element tableNode = (Element) root.selectSingleNode("//c:Tables");
        for(Element e:tableNode.elements()){
            //判断是否有主键
            Element keys = e.element("Keys");
            if(keys != null){
                String PKId = keys.element("Key").element("Key.Columns").element("Column").attributeValue("Ref");
                allPKIds.add(PKId);
            }
        }
        return allPKIds;
    }
    /**
     * 加载table的codeLib
     */
    public Map<String,String> loadTableCodeLib(){
        Map<String,String> tableCodeLib = new HashMap<String, String>();
        Element tableNode = (Element) root.selectSingleNode("//c:Tables");
        for(Element t:tableNode.elements()){
            String id = t.attributeValue("Id");
            String name = t.elementText("Name");
            String code = t.elementText("Code");
            tableCodeLib.put(id, name+code);
        }
        return tableCodeLib;
    }
    /**
     * 加载所有的表
     * @param args
     */
    public List<Table> loadAllTables(){
        List<Table> allTables = new ArrayList<Table>();
        //主键
        //List<String> allPKIds = loadAllPKIds();
        //外键
        //List<Reference> allReferences = loadAllReferences();
        //作用域
        //List<Domain> allDomains = loadAllDomains();

        Element tableNode = (Element) root.selectSingleNode("//c:Tables");
        for(Element t:tableNode.elements()){
            Table table = new Table();
            List<Column> allColumns = new ArrayList<Column>();
            String tableId = t.attributeValue("Id");
            String tableName = t.elementText("Name");
            String tableCode = t.elementText("Code");
            System.out.println("============tableName========"+tableName+"====tableCode=="+tableCode);
            //1
            table.setTableId(tableId);
            //2
            table.setTableName(tableName);
            //3
            table.setTableCode(tableCode);
            //Column信息添加
            Element columnNode = t.element("Columns");
            if(columnNode !=null){
            	for(Element col:columnNode.elements()){
                    Column column = new Column();
                    String columnId = col.attributeValue("Id");
                    String columnName = col.elementText("Name");
                    String columnCode = col.elementText("Code");
                    String columnComment = col.elementText("Comment");
                    String columnType=col.elementText("DataType");
                    //String domainId = col.element("Domain").element("PhysicalDomain").attributeValue("Ref");
                    String columnDomain = "";
                    //根据domainId获取domainName
                    /*for(Domain d : allDomains){
                        if(domainId.equals(d.getDomainId())){
                            columnDomain = d.getDomainName();
                        }
                    }*/
                    boolean PK = false;
                    //获取主键
                    /*if(allPKIds.contains(columnId)){
                        PK = true;
                    }*/
                    boolean FK = false;
                    //获取外键
                   /* for(Reference ref : allReferences){
                        if(columnId.equals(ref.getFKId())){
                            FK = true;
                            //4
                            table.setParentTableId(ref.getParentId());
                        }
                    }*/
                    column.setColId(columnId);
                    column.setColName(columnName);
                    column.setColCode(columnCode);
                    column.setColComment(columnComment);
                    column.setColDomain(columnDomain);
                    column.setColType(columnType);
                    column.setPK(PK);
                    column.setFK(FK);   
                    allColumns.add(column);
                }
            }
            
            //5
            table.setAllColumns(allColumns);
            allTables.add(table);
        }
        return allTables;
    }
    public void printAllTables(Statement statement,List<Table> allTables) throws SQLException{
        //表的id和name的表
        Map<String,String> tableCodeLib = loadTableCodeLib();
        for(Table table : allTables){
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("表:"+table.getTableName()+table.getTableCode());
            for(Entry<String, String> entry:tableCodeLib.entrySet()){
                if(entry.getKey().equals(table.getParentTableId())){
                    System.out.println("parentTable:"+entry.getValue());
                }
            }
            String tablePK = "";
            String tableFK = "";
            for(Column column : table.getAllColumns()){
                System.out.println("字段:"+column.getColName()+column.getColCode()+","+"注释:"+column.getColComment()+","+"domain:"+column.getColDomain());
                //if(column.isPK())tablePK += column.getColName()+column.getColCode();
                //if(column.isFK())tableFK += column.getColName()+column.getColCode();
                
                //  String sql="insert into test_pdm values('','"+DanYinHao(filename)+"','"+DanYinHao(table.getTableCode())+"','"+DanYinHao(table.getTableName())+"','"+DanYinHao(column.getColCode())+"','"+DanYinHao(column.getColName())+"','"+"',''"+")";
                  String sql="insert into test_pdm values('','"+DanYinHao(filename)+"','"+DanYinHao(table.getTableCode())+"','"+DanYinHao(table.getTableName())+"','"+DanYinHao(column.getColCode())+"','"+DanYinHao(column.getColName())+"',''"+",'"+DanYinHao(column.getColType())+"'"+")";

                System.out.println("=================sql===="+sql);
                
                statement.execute(sql);
            }
            System.out.println("主键:"+tablePK+"外键:"+tableFK);
        }
    }
    
    
    public String DanYinHao(String str) {
    	if(str!=null&&!"".equals(str)){
    		 return str.replaceAll("\r|\n|\'","");
    	}else {
    		 return "";
    	}
    }
    
    public static void main(String[] args)  {
    	
    	 Connection connection=null;
         Statement statement =null;
         
        try {
            long start = System.currentTimeMillis();
            String url="jdbc:oracle:thin:@//localhost:1521/orcl";
            String user="scott";
            String password = "tiger";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection= DriverManager.getConnection(url, user, password);
            System.out.println("是否成功连接oracle数据库"+connection);
            statement=connection.createStatement();
            
            ParsePDM parsePDM = new ParsePDM();
            List<Table> allTables = parsePDM.loadAllTables();
            long end = System.currentTimeMillis();
            parsePDM.printAllTables(statement,allTables);
            System.out.println("用时:"+(end-start));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally{
            try{
                statement.close();
            }
            catch(SQLException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally{
                try{
                    connection.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}