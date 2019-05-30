package external

import org.w3c.dom.Element

external fun includeHTML(file: String)

external fun includeHTML(file: String, element: Element?)

external fun includeHTML(file: String, element: Element?, onSuccess: () -> Unit)
