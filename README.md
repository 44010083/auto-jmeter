自动生成REST API接口jmeter测试用例的工具，计划支持以下2种场景：  
1、输入使用aut工具生成的ut用例数据  
2、输入标准的REST API定义数据（例如：来自swagger的API定义数据）  

  
操作步骤：  
1、由输入数据生成jmeter用例模板（甚至是已经初始化部分参数的用例文件）  
2、拷贝用例模板，生成新的用例数据文件，并编辑此文件，完善文件所需数据  
3、生成jmeter测试工程（含多个测试用例文件生成，执行测试的pom.xml生成）
