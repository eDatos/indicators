[#ftl strip_whitespace=true]

[#--
 * messageEscape indicating escape text  (default = false)
 *
 * Macro to translate a message code with arguments into a message.
 --]
[#macro messageEscape code, escape=false]
	[#assign args = [] /]
	${springMacroRequestContext.getMessage(code, args, '', escape)}
[/#macro]