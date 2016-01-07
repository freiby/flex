<table class="table table-hover">
   <caption>悬停表格布局</caption>
   <thead>
      <tr>
         <th>名称</th>
         <th>城市</th>
         <th>密码</th>
      </tr>
   </thead>
   <tbody>
      <tr>
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
      </tr>
   </tbody>
</table>
<script type="text/javascript">
   (function (){
      ready(
            function(){
            var data = $page.data('page'); //找到page
            var view = data.findViewById('com.wxxr.nirvana.test.table');//从page里面得到view 这个view就是当前view
            view['selectChanage'] = function(e){//注册监听函数，得到某个view选择的事件
                  console.log("view: " + e.part.id + ", type: " + e.type  + ", selected: " + e.selection );
               };
            }
         )
   })();
   
   

</script>