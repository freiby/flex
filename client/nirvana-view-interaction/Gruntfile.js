
module.exports = function(grunt) {
    // 配置
    grunt.initConfig({
        pkg : grunt.file.readJSON('package.json'),
        concat : {
            domop : {
                src: ['src/*.js','src/**/*.js'],
                dest: 'dest/nirvana-view-interaction.js'
            }
        },
        uglify : {
            options : {
                banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build : {
                src : 'dest/nirvana-view-interaction.js',
                dest : 'dest/nirvana-view-interaction.min.js'
            }
        },
		jshint : {
			build : ['Gruntfile.js','src/*.js'],
			options : {
				jshintrc : '.jshintrc'
			}
		},
		watch: {
		  build: {
		    files: ['src/*.js'],
		    tasks: ['jshint'],
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