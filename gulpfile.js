var gulp = require("gulp");
var babel = require("gulp-babel");
var browserify = require("browserify");
var source = require('vinyl-source-stream');

gulp.task("build", function () {
  return browserify("./index-view/src/main/resources/static/js/app.js")
  .transform("babelify", {presets: ["es2015"]})
  .bundle()
  .pipe(source("bundle.js"))
  .pipe(gulp.dest('./index-view/src/main/resources/static/js/'));
});

gulp.task('watch', ['build'], function () {
    gulp.watch('./index-view/src/main/resources/static/js/*.js', ['build']);
});

gulp.task('default', ['watch']);