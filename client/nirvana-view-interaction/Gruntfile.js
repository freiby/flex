
module.exports = function(grunt) {
    // 配置
    grunt.initConfig({
        pkg : grunt.file.readJSON('package.json'),
        concat : {
            domop : {
                src: ['src/js/page.selection.service.js','src/js/nirvana.view.interaction.js'],
                dest: 'build/nirvana.view.interaction.js'
            }
        },
        uglify : {
            options : {
				mangle: false, //不混淆变量名
				preserveComments: 'all', //不删除注释，还可以为 false（删除全部注释），some（保留@preserve @license @cc_on等注释）
                banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            builda : {
                src : 'build/nirvana.view.interaction.js',
                dest : 'build/nirvana.view.interaction.min.js'
            },

            buildb : {
                src : 'src/js/nirvana.init.js',
                dest : 'build/nirvana.init.min.js'
            },

			buildc : {
                src : 'src/js/loading/jquery.showLoading.js',
                dest : 'build/jquery.showLoading.min.js'
            },
			buildd : {
                src : 'src/js/nirvana.rpc.js',
                dest : 'build/nirvana.rpc.min.js'
            }
        },
		jshint : {
			build : ['Gruntfile.js','src/js/**/*.js'],
			options : {
				jshintrc : '.jshintrc'
			}
		},
		watch: {
		  build: {
		    files: ['src/js/**/*.js'],
		    tasks: ['jshint','concat','uglify'],
		    options: {
		      spawn: false,
		    }
		  }
		}
    });
    // 载入concat和uglify插件，分别对于合并和压缩
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-watch');
    // 注册任务
    grunt.registerTask('default', ['jshint', 'concat', 'uglify' , 'watch']);
};