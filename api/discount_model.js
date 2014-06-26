"use strict";
var geolib = require('./geolib');
var BaseTable = require('./base_model');
var R = {};
function Discount() {
	BaseTable.call(this);
	this.getModel = function() {
		return this;
	}
}

Discount.prototype = new BaseTable();
Discount.prototype.constructor = Discount;
Discount.prototype.name = "categories";
Discount.prototype.getAll = function(req, res, next, callback) {
	var latitude = parseFloat(req.params.lat);
	var longitude = parseFloat(req.params.lng);
	var d = parseFloat(req.params.radius);
	var bounds = geolib.getBoundsOfDistance({latitude: latitude, longitude: longitude}, d);
	console.log(bounds);
    R.pgsql.connect(R.pgsqlUrl, function(err, client) {
    	if (err) { 
			return next(err);
		}
		console.log([bounds[0].latitude,bounds[1].latitude,bounds[0].longitude,bounds[1].longitude]);
		client.query("SELECT id,name,address,cardType,latitude,longitude FROM isic WHERE $1 < latitude AND $2 > latitude AND $3 < longitude AND $4 > longitude;", [parseFloat(bounds[0].latitude),parseFloat(bounds[1].latitude),parseFloat(bounds[0].longitude),parseFloat(bounds[1].longitude)], function(err, results, fields) {
			if (err) { 
				return next(err);
			}
			callback(results.rows);
			return next();
		});
	});
}
Discount.prototype.delete = function(req, res, next) {
	BaseTable.prototype.delete.apply(this, Array.prototype.slice.call(arguments));
}

module.exports.createClient = function(resource) {
	R = resource;
	return Discount;
}