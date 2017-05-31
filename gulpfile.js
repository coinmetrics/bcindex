var gulp = require("gulp");
var babel = require("gulp-babel");
var browserify = require("browserify");
var fs = require("fs");

gulp.task("default", function () {
  return browserify("index-view/src/main/resources/static/js/app.js")
  .transform("babelify", {presets: ["es2015"]})
  .bundle()
  .pipe(gulp.dest("index-view/src/main/resources/static/js/dist/"));
});