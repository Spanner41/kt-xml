package design.cardia.ktxml.printer

import design.cardia.ktxml.model.XmlVersion
import org.apache.commons.lang3.StringEscapeUtils.escapeXml10
import org.apache.commons.lang3.StringEscapeUtils.escapeXml11

sealed class XmlFormat(
    val collapseEmptyTags: Boolean = true,
    val escapeFormat: EscapeFormat = EscapeFormat.DECIMAL,
    val lineSeparator: String
) {
    fun escape(text: String, xmlVersion: XmlVersion): String {
        return when (escapeFormat) {
            EscapeFormat.REFERENCE -> text.escapeUsingReferences()
            EscapeFormat.DECIMAL -> {
                when (xmlVersion) {
                    XmlVersion.V1_1 -> escapeXml11(text)
                    XmlVersion.V1_0 -> escapeXml10(text)
                }
            }
        }
    }

    private fun String.escapeUsingReferences(): String {
        return toCharArray().map {
            when (it) {
                '\'' -> "&#39;"
                '&' -> "&#38;"
                '<' -> "&#60;"
                '>' -> "&#62;"
                '"' -> "&#34;"
                else -> it
            }
        }.joinToString(separator = "")
    }
}

class PrettyFormat(
    val indentString: String = "  ",
    collapseEmptyTags: Boolean = true,
    val textOnNewLine: Boolean = false,
    escapeFormat: EscapeFormat = EscapeFormat.REFERENCE
) : XmlFormat(collapseEmptyTags, escapeFormat, "\n")

class CompressedFormat(
    collapseEmptyTags: Boolean = true,
    escapeFormat: EscapeFormat = EscapeFormat.DECIMAL
) : XmlFormat(collapseEmptyTags, escapeFormat, "")

enum class EscapeFormat {
    DECIMAL, REFERENCE
}
