package design.cardia.ktxml.printer

import design.cardia.ktxml.model.Element
import design.cardia.ktxml.model.Node
import design.cardia.ktxml.model.Text
import design.cardia.ktxml.model.XmlVersion

class ElementPrinter {
    fun print(
        node: Element,
        format: XmlFormat,
        version: XmlVersion,
        separator: String,
        postfix: String,
        print: Node.() -> String,
    ) =
        if (node.shouldCollapse(format)) {
            node.selfClosingTag(format, version)
        } else {
            node.openTag(format, version) +
                node.childrenToXml(
                    format,
                    separator,
                    postfix,
                    print,
                ) +
                node.closeTag()
        }

    private fun Element.openTag(format: XmlFormat, version: XmlVersion) =
        "<${type}${attributesToXml(format, version)}>"

    private fun Element.closeTag() =
        "</$type>"

    private fun Element.selfClosingTag(format: XmlFormat, version: XmlVersion) =
        "<${type}${attributesToXml(format, version)} />"

    private fun Element.shouldCollapse(format: XmlFormat) =
        canCollapseWhenEmpty && format.collapseEmptyTags && children.isEmpty()

    // Should this be part of the XML printer?
    private fun Element.childrenToXml(
        format: XmlFormat,
        separator: String,
        postfix: String,
        print: Node.() -> String,
    ) = if (format is PrettyFormat && !format.textOnNewLine && children.filterIsInstance<Text>().isNotEmpty()) {
        children.joinToString(
            separator = "",
            transform = print,
        ).ifBlank { "" }
    } else {
        children.joinToString(
            separator = separator,
            prefix = separator,
            postfix = postfix,
            transform = print,
        ).ifBlank { "" }
    }
}
