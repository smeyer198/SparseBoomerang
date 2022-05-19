package boomerang.scene.sparse;

import com.google.common.graph.MutableGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Unit;
import soot.Value;

public class SparseAliasingCFG {

  private static Logger log = LoggerFactory.getLogger(SparseAliasingCFG.class);

  private MutableGraph<Unit> graph;
  private Value d; // which dff this SCFG belongs to
  private Unit queryStmt; // in contrast to sparseCFG queryStmt affects the graph
  private Set<Value> fallbackAliases;

  public SparseAliasingCFG(
      Value d, MutableGraph<Unit> graph, Unit queryStmt, Set<Value> fallbackAliases) {
    this.d = d;
    this.queryStmt = queryStmt;
    this.graph = graph;
    this.fallbackAliases = fallbackAliases;
  }

  public Set<Value> getFallBackAliases() {
    return fallbackAliases;
  }

  public synchronized boolean addEdge(Unit node, Unit succ) {
    return graph.putEdge(node, succ);
  }

  public Set<Unit> getSuccessors(Unit node) {
    return graph.successors(node);
  }

  public List<Unit> getNextUses(Unit node) {
    Set<Unit> successors = getSuccessors(node);
    return new ArrayList<>(successors);
  }

  public MutableGraph<Unit> getGraph() {
    return this.graph;
  }
}
