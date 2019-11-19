var exec = require('cordova/exec');

exports.activateSensor = function(mode, success, error) {
    success = success || function() { };
    error = error || function() { };

    exec(success, error, "ProximityWake", "activate", [mode]);
};


exports.deactivateSensor = function(success, error) {
    success = success || function() { };
    error = error || function() { };

    exec(success, error, "ProximityWake", "deactivate");
};
