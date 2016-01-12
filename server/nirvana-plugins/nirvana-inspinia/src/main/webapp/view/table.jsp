<table class="table table-hover">
   <caption>悬停表格布局</caption>
   <thead>
      <tr>
         <th>名称</th>
         <th>城市</th>
         <th>密码</th>
      </tr>
   </thead>
   <tbody id="citybody">
     <!--  <tr>
         <td>Tanmay</td>
         <td>Bangalore</td>
         <td>560001</td>
      </tr>
      <tr>
         <td>Sachin</td>
         <td>Mumbai</td>
         <td>400003</td>
      </tr>
      <tr>
         <td>Uma</td>
         <td>Pune</td>
         <td>411027</td>
      </tr> -->
   </tbody>
</table>
<script type="text/javascript">
   (function (){
      ready(
            function(){
            var data = $page.data('page'); //找到page
            var view = data.findViewById('com.wxxr.nirvana.test.table');//从page里面得到view 这个view就是当前view
            var selection = null;
            view['selectChanage'] = function(e){//注册监听函数，得到某个view选择的事件
                  console.log("view: " + e.part.id + ", type: " + e.type  + ", selected: " + e.selection );
                  if(selection == e.selection){
                    return;
                  }
                  selection = e.selection;
                  var ajaxurl = "doRpc.action";
                  
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
                  // params.push(bean);
                  // params.push(map);
                  // params.push(array);
                  params.push(e.selection);
                  //(ajaxurl, method, params, rpcId, successCb, errorCb)
                  var foo = new $.invorkRpc(ajaxurl, 'getList', params, "com.wxxr.nirvana.test.invorkAction", 
                    function(result) { 
                        console.log('Foo bar answered: ' + result); 
                        var $body = $('#citybody');
                        $body.empty();
                        for(var i in result){
                          var $tr = $('<tr/>');
                          
                          var city = result[i];
                          for(var j=0; j<3; j++){
                             var $td = $('<td/>')
                             $td.text(city.name);
                             $tr.append($td);
                          }
                         
                          $body.append($tr);
                        }

                      },
                    function(error)  { 
                        console.log('There was an error', error); 
                      },
                      true
                  );
               };
            }
            
            
            
            
            
         )
   })();
   
   

</script>