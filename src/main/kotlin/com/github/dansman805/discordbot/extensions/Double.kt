package com.github.dansman805.extensions

val Double.pretty: String
    get() = "${this}0000".substring(0, this.toString().indexOf(".") + 4)