<div class="listNav" data="zhaoer">
	<a href="#" class="list-group-item active">
   	赵二
	</a>
	<a href="#" class="list-group-item" data="zhangsan">张三</a>
	<a href="#" class="list-group-item" data="lisi">李四</a>
	<a href="#" class="list-group-item" data="wangwu">王五</a>

</div>

<script type="text/javascript">
	ready(function(){
		$('.listNav>a').on('click',function(e){
			$('.listNav>a').each(function(e){
				$(this).removeClass('active');
				// $(this).addClass('list-group-item');

			});
			$(this).addClass('active');
			var data = $page.data('page'); //找到page
	  		var view = data.findViewById('com.wxxr.nirvana.test.nav');//从page里面得到view 这个view就是当前view
	   		view.setSelection($(this).attr('data'));
		});
	});
	
</script>
