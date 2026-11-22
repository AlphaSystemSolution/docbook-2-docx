package com.alphasystem.xml;

import jakarta.xml.bind.Binder;
import jakarta.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public final class ParsedDocBook {

  private final Document document;
  private final Object root;
  private final Map<Node, Object> objects = new IdentityHashMap<>();
  private final Map<Object, Node> nodes = new IdentityHashMap<>();
  private final List<ProcessingInstruction> instructions = new ArrayList<>();

  ParsedDocBook(Document document, Binder<Node> binder, Object root) {
    this.document = document;
    this.root = unwrap(root);
    index(document, binder);
  }

  private void index(Node node, Binder<Node> binder) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      final var object = unwrap(binder.getJAXBNode(node));
      if (object != null) {
        objects.put(node, object);
        nodes.put(object, node);
      }
    } else if (node instanceof ProcessingInstruction instruction) {
      instructions.add(instruction);
    }
    for (var child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      index(child, binder);
    }
  }

  public Document getDocument() {
    return document;
  }

  public Object getRoot() {
    return root;
  }

  public Object getObject(Node node) {
    return objects.get(node);
  }

  public Node getNode(Object object) {
    return nodes.get(unwrap(object));
  }

  public List<ProcessingInstruction> getInstructions() {
    return List.copyOf(instructions);
  }

  public static Object unwrap(Object object) {
    while (object instanceof JAXBElement<?> element) {
      object = element.getValue();
    }
    return object;
  }
}
