"use strict";
var testing_1 = require('@angular/core/testing');
var media_planner_component_1 = require('../app/media-planner.component');
testing_1.beforeEachProviders(function () { return [media_planner_component_1.MediaPlannerAppComponent]; });
testing_1.describe('App: MediaPlanner', function () {
    testing_1.it('should create the app', testing_1.inject([media_planner_component_1.MediaPlannerAppComponent], function (app) {
        testing_1.expect(app).toBeTruthy();
    }));
    testing_1.it('should have as title \'media-planner works!\'', testing_1.inject([media_planner_component_1.MediaPlannerAppComponent], function (app) {
        testing_1.expect(app.title).toEqual('media-planner works!');
    }));
});
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/media-planner.component.spec.js.map