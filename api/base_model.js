"use strict";

function BaseTable() {
}

BaseTable.prototype.name = null;

BaseTable.prototype.getAll = function(req, res, next) {};

BaseTable.prototype.get = function(req, res , next){}

BaseTable.prototype.create = function(req, res, next) {}

BaseTable.prototype.delete = function(req, res, next) {}

BaseTable.prototype.getModel = function() {}

module.exports = BaseTable;