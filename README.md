# hbase-command
hbase客户端相关操作封装成服务，采用http接口调用。内部采用命令模式

获取Hbase数据 API文档


访问方式http
入口: http://192.168.10.99:8080/service/query?serviceType=?&requestInfo=?
参数名称	参数类型	是否必填	默认值	取值说明
serviceType	String	否	1	取值：1或2，表示不同的业务逻辑，1标示本次的Hbase查询相关的服务，2只是一个空壳子，还未实现，以便以后扩展使用
requestInfo	json	是		见下表解释


requestInfo  JSON参数说明:
参数名称	参数类型	是否必填	默认值	取值说明
commondId	int	是	0	取值范围从1-10,
1:创建表
2:删除表
3:查询表是否存在
4：删除表中一条记录的所有列(删一条数据)
5:删除表中一条记录的一列
6:修改表中一条记录一个列下的一个字段的值
7:像表中添加数据
8:查询表中的数据(如果rowKeys为空则查询表中的所有数据,如果columnNames是空则返回所有表中的字段)
9:查询startRowKey 至endRowKey的数据
10:查询一条记录一个列下的一个字段的值

tableName	String	是	Null	表名
columnNames	Set<String>	否	[]	返回的字段名称集合，可以通过此参数返回自定义字段
isNeedRowKey	boolean	否	False	是否需要返回rowKey，默认不需要,查询时用
maxSize	long	否	0	返回的最大记录数，0标示全部返回，查询时用
rowKeys	List<String>	否	[]	主键的集合(可以根据主键查询数据或删除数据)
startRowKey	String	否		开始主键(查询用，包含此项)
endRowKey	String	否		结束主键(查询用,不包含此项)
columnBean	{   
rowKe主键rowKey
familyName: 列族名称
columnName: 字段名称
columnValue: 字段值
 }	是		删除列修改列使用
beginTime	long	否	0	数据的开始时间(时间戳，和结束时间成对使用)
endTime	long	否	0	数据的结束时间(时间戳，和开始时间成对使用)
family	List<String>	是	Null	添加时需要,标示Hbase的列族名称，为了性能,不要超过
records	[{
rowKey: 主键rowKey
familyName: 列族名称
columnData:数据
}]	是	Null	添加数据时使用

使用示例:
http://192.168.10.99:8080/service/query?serviceType=1&requestInfo= {"tableName":"user_feature_category","columnNames":[],"rowKeys":[],"commandId":8,"maxSize":10} 


返回结果说明:
返回的结果是JSON格式的数据,
返回json的key	取值	说明
statusCode		0标示成功，-1标示失败
msg	“”	信息，当成功时msg是空,失败时是失败的详细信息
valueType		返回的类型值,1标示list类型，2标示string类型，3标示map类型
strData	String	字符串类型返回值
listData	List<?>	list类型返回值，一般返回是{@code List<Map<String,String>>}
mapData	Map<String,?>	map类型返回值
ext	Object	扩展字段

