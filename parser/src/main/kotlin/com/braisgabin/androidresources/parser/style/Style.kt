package com.braisgabin.androidresources.parser.style

data class Style(val name: String, val parent: String? = null, val items: List<Item> = emptyList()) {

  data class Item(val name: String, val value: String)
}
