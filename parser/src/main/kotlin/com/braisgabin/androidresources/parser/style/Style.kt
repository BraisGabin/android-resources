package com.braisgabin.androidresources.parser.style

import com.braisgabin.androidresources.parser.Resource

data class Style(
    val name: String,
    override val directory: String,
    override val fileName: String,
    val parent: String? = null,
    val items: List<Item> = emptyList()
) : Resource {

  data class Item(val name: String, val value: String)
}
