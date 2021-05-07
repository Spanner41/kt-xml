package design.cardia.ktxml.printer

import design.cardia.ktxml.builder.Element
import design.cardia.ktxml.builder.XmlFormat
import design.cardia.ktxml.builder.XmlVersion

class ElementPrintHelper {
  fun openTag(node: Element, format: XmlFormat, version: XmlVersion) =
    "<${node.type}${node.attributesToXml(format, version)}>"

  fun closeTag(node: Element) =
    "</${node.type}>"

  fun selfClosingTag(node: Element, format: XmlFormat, version: XmlVersion) =
    "<${node.type}${node.attributesToXml(format, version)} />"

  fun shouldCollapse(node: Element, format: XmlFormat) =
    node.canCollapseWhenEmpty && format.collapseEmptyTags && node.children().isEmpty()
}