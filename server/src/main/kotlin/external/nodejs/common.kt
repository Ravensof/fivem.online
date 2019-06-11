package external.nodejs

fun requireNodeJSModule(module: String) = require(module)

private external fun require(module: String): dynamic