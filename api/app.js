var restify = require('restify');
var restifyOAuth2 = require("restify-oauth2");
var server = restify.createServer({name : "discountapi"});
var R = require('./R');
var pgsqlReq = require('pg');
R.pgsql = pgsqlReq;
R.pgsqlUrl = "postgres://nxxclbdnrjiwcx:pz1YC2j_lfM4YBuV3iLFyV7c_p@ec2-54-197-251-18.compute-1.amazonaws.com:5432/d1gcms75s3ef0g?ssl=true&sslmode=require";
var Discount = require('./discount_model').createClient(R);

var discount = new Discount();
var discountCache = discount;


// SERVER
/*var ip = '37.157.193.199';*/
server.listen(R.port , R.ip, function(){
    console.log('%s listening at %s ', server.name , server.url);
});

server.use(restify.authorizationParser());
server.use(restify.queryParser());
server.use(restify.bodyParser());
server.use(restify.CORS());

// ROUTING
var PATH = null;
var ENCODING = 'utf-8';
var APIVERSION = '0.0.1';
var callback = function(value){return value;};
var common = function(req, res, next) {
	res.charSet = ENCODING; 
}
   
// Places
PATH = '/places'
	server.get({path : PATH + '/:lat/:lng/:radius', version : APIVERSION} , function(req, res , next){ 
		res.charSet(ENCODING);
		return discount.getAll(req, res , next, function(results) {
			res.send(200 , results);
		});  
	});
	server.get({path : PATH +'/:id' , version : APIVERSION} , function(req, res , next){ 
		throw "not implemented!"
	});
	server.post({path : PATH , version: APIVERSION} , function(req, res , next){ 
		throw "not implemented!"
	});
	server.del({path : PATH +'/:id' , version: APIVERSION} , function(req, res , next){ 
		throw "not implemented!" 
	});
