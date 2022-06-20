package boomerang.scene.sparse;

import boomerang.scene.Field;
import boomerang.scene.Method;
import boomerang.scene.Statement;
import boomerang.scene.Val;
import boomerang.scene.jimple.*;
import soot.*;
import soot.jimple.Stmt;

public class SootAdapter {

  public static Statement asStatement(Unit unit, Method method) {
    return JimpleStatement.create((Stmt) unit, method);
  }

  public static Stmt asStmt(Statement stmt) {
    return ((JimpleStatement) stmt).getDelegate();
  }

  public static Type getTypeOfVal(Val val) {
    if (val instanceof JimpleVal) {
      Value value = asValue(val);
      return value.getType();
    } else if (val instanceof JimpleStaticFieldVal) {
      SootField field = asField(val);
      return field.getType();
    } else {
      throw new RuntimeException("Unknown Val");
    }
  }

  public static Value asValue(Val val) {
    if (val instanceof JimpleStaticFieldVal) {
      throw new RuntimeException("handle this");
    }
    return ((JimpleVal) val).getDelegate();
  }

  public static SootField asField(Val val) {
    Field field = ((JimpleStaticFieldVal) val).field();
    return ((JimpleField) field).getSootField();
  }

  public static SootMethod asSootMethod(Method m) {
    return ((JimpleMethod) m).getDelegate();
  }
}
