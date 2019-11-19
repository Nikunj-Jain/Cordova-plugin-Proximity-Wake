var exec = require('cordova/exec');

exports.activateSensor = function(success, error) {
    setSensorMode("activate", success, error);
};


exports.deactivateSensor = function(success, error) {
    setSensorMode("deactivate", success, error);
};

private function setSensorMode(modeStr, success, error) {
    success = success || function() { };
    error = error || function() { };

    exec(success, error, "ProximityWake", modeStr);
}
