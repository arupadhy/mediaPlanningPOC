/***********************************************************************************************
 * User Configuration.
 **********************************************************************************************/
/** Map relative paths to URLs. */
var map = {};
/** User packages configuration. */
var packages = {};
////////////////////////////////////////////////////////////////////////////////////////////////
/***********************************************************************************************
 * Everything underneath this line is managed by the CLI.
 **********************************************************************************************/
var barrels = [
    // Angular specific barrels.
    '@angular/core',
    '@angular/common',
    '@angular/compiler',
    '@angular/http',
    '@angular/router',
    '@angular/platform-browser',
    '@angular/platform-browser-dynamic',
    // Thirdparty barrels.
    'rxjs',
    'lodash',
    // App specific barrels.
    'app',
    'app/shared',
    'app/+dashboard',
    'app/+clients',
    'app/+home',
    'app/+clientbrief',
    'app/+mediaplan',
    'app/+mediaplan/+mediaplan-summary',
    'app/+concepts',
    'app/suggestion',
    'app/+suggestion',
    'app/querybuilder',
    'app/cycquestion',
    'app/+mriquestion',
    'app/+mrifields',
    'app/cycavatar',
    'app/planprogress',
    'app/output',
];
var cliSystemConfigPackages = {};
barrels.forEach(function (barrelName) {
    cliSystemConfigPackages[barrelName] = { main: 'index' };
});
// Apply the CLI SystemJS configuration.
System.config({
    map: {
        '@angular': 'vendor/@angular',
        'rxjs': 'vendor/rxjs',
        'lodash': 'vendor/lodash',
        'main': 'main.js'
    },
    packages: cliSystemConfigPackages
});
// Apply the user's configuration.
System.config({ map: map, packages: packages });
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/system-config.js.map