marked = require 'marked'
handlebars = require 'handlebars'
hljs = require 'highlight.js'

marked.setOptions
	gfm: true
	breaks: true
	langPrefix: 'language-'
	sanatize: false
	highlight: (code, lang) ->
		# quick and dirty language aliases
		lang = 'xml' if lang is 'html'
		lang = 'javascript' if lang is 'js'
 
		code = hljs.highlight(lang, code).value if lang

		code

# setup a helper for Handlebars so that Markdown can be parsed within templates
handlebars.registerHelper 'marked', (content) ->
	return marked content

# wrap content in an header tag and parse the markdown
handlebars.registerHelper 'marked-heading', (size, content) ->
	heading = "<h#{size}>#{content}</h#{size}>"
	return marked heading

# make the iteration index available for each blocks
handlebars.registerHelper 'each', (content, options) ->
	result = ""

	result = (for item, i in content
		item._i = i+1
		options.fn item
	).join('')

handlebars.registerHelper 'equal', (value1, value2, options) ->
	if value1 is value2 
		options.fn(this) 
	else 
		options.inverse(this)

# conditional that checks whether this is the first element that's being iterated over
handlebars.registerHelper 'first', (options) ->
	return unless this._i?
	options.fn(this) if this._i is 1

# capatilize the first letter of a string
handlebars.registerHelper 'ucfirst', (value, options) ->
	return value.charAt(0).toUpperCase() + value.slice(1)

# log helper for debugging
handlebars.registerHelper 'log', (value) ->
	console.log value

module.exports = handlebars