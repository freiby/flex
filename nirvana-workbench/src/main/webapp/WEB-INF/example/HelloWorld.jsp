<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title><s:text name="HelloWorld.message"/></title>
</head>

<body>
<h2><s:property value="message"/></h2>

<h3>Languages</h3>
<ul>
    <li>
        <s:url id="url" action="HelloWorld">
            <s:param name="request_locale">en</s:param>
        </s:url>
        <s:a href="%{url}">English</s:a>
    </li>
    <li>
        <s:url id="url" action="HelloWorld">
            <s:param name="request_locale">es</s:param>
        </s:url>
        <s:a href="%{url}">Espanol</s:a>
    </li>
</ul>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.jsonrpcclient.js"></script>
<script type="text/javascript">
    var ajaxurl = "sdmByAction.action"
    var foo = new $.JsonRpcClient({ajaxUrl: ajaxurl });
    params = [];
    var bean = {};
    bean.booleanField = true;
    bean.stringField = 'test';
    bean.intField = 10;
    bean.charField = 's';
    bean.doubleField = 10.1;
    bean.byteField = 3;
    var map = {};
    map.a = "1";
    map.c = [1.0, 2.0];
    var array = ["str0", "str1"];
    params.push(bean);
    params.push(map);
    params.push(array);

    foo.call(
      'doSomethingObjects', params,
      function(result) { 
        alert('Foo bar answered: ' + result); 
        },
      function(error)  { 
        console.log('There was an error', error); 
        }
    );

</script>
</body>
</html>
