package typestate.impl.statemachines.alloc;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import boomerang.jimple.Val;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import typestate.finiteautomata.MatcherStateMachine;
import typestate.finiteautomata.MatcherTransition;
import typestate.finiteautomata.MatcherTransition.Parameter;
import typestate.finiteautomata.MatcherTransition.Type;
import typestate.finiteautomata.State;

public class OutputStreamStateMachine extends MatcherStateMachine{

	public static enum States implements State {
		OPEN, CLOSED, ERROR;

		@Override
		public boolean isErrorState() {
			return this == ERROR;
		}

		@Override
		public boolean isInitialState() {
			return false;
		}

	}

	OutputStreamStateMachine() {
		addTransition(
				new MatcherTransition(States.OPEN, closeMethods(), Parameter.This, States.CLOSED, Type.OnReturn));
		addTransition(
				new MatcherTransition(States.CLOSED, closeMethods(), Parameter.This, States.CLOSED, Type.OnReturn));
		addTransition(new MatcherTransition(States.OPEN, writeMethods(), Parameter.This, States.OPEN, Type.OnReturn));
		addTransition(new MatcherTransition(States.CLOSED, writeMethods(), Parameter.This, States.ERROR, Type.OnReturn));
		addTransition(new MatcherTransition(States.ERROR, writeMethods(), Parameter.This, States.ERROR, Type.OnReturn));
	}


	private Set<SootMethod> constructors() {
		List<SootClass> subclasses = getSubclassesOf("java.io.OutputStream");
		Set<SootMethod> out = new HashSet<>();
		for (SootClass c : subclasses) {
			for (SootMethod m : c.getMethods())
				if (m.isConstructor())
					out.add(m);
		}
		return out;
	}
	private Set<SootMethod> closeMethods() {
		return selectMethodByName(getImplementersOf("java.io.OutputStream"), "close");
	}

	private Set<SootMethod> writeMethods() {
		return selectMethodByName(getImplementersOf("java.io.OutputStream"), "write");
	}


	private List<SootClass> getImplementersOf(String className) {
		SootClass sootClass = Scene.v().getSootClass(className);
		List<SootClass> list = Scene.v().getActiveHierarchy().getSubclassesOfIncluding(sootClass);
		List<SootClass> res = new LinkedList<>();
		for (SootClass c : list) {
			res.add(c);
		}
		return res;
	}

	@Override
	public Collection<Val> generateSeed(SootMethod method, Unit unit,
			Collection<SootMethod> calledMethod) {
		return generateThisAtAnyCallSitesOf(method, unit,calledMethod,constructors());
	}

}