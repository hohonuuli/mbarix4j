/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mbari.reflect;

import java.lang.reflect.*;
import java.util.*;

/**
 * From http://www.vitarara.org/cms/whats_wrong_with_javas_dynamic_dispatch_or_how_i_implemented_sendMessage
 */
public class ReflectionTest {

    public static void main(String[] args) {
        try {
//      A a = new A ();
//      B b = new B ();
//      C c = new C ();
//      sendMessage ("execute", c, new Object[] { b });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object sendMessage(String message, Object target, Object[] args) {
        try {
            // Is this an argumentless method call?
            if (args == null) {
                // Get the method.
                return target.getClass().getMethod(message, null).invoke(target, null);
            }
            else {

                // Get all methods from the target.
                Method[] allMethods = target.getClass().getMethods();
                List candidateMethods = new ArrayList();

                for (int i = 0; i < allMethods.length; i++) {
                    // Filter methods by name and length of arguments.
                    Method m = allMethods[i];
                    if (m.getName().equals(message) && m.getParameterTypes().length == args.length) {
                        candidateMethods.add(m);
                    }
                }

                if (candidateMethods.size() == 0) {
                    throw new RuntimeException("");
                }

                Method callableMethod = null;
                for (Iterator itr = candidateMethods.iterator(); itr.hasNext();) {
                    boolean callable = true;
                    Method m = (Method) itr.next();
                    Class[] argFormalTypes = m.getParameterTypes();
                    for (int i = 0; i < argFormalTypes.length; i++) {
                        if (!argFormalTypes[i].isAssignableFrom(args[i].getClass())) {
                            callable = false;
                        }
                    }
                    if (callable) {
                        callableMethod = m;
                    }
                }

                if (callableMethod != null) {
                    return callableMethod.invoke(target, args);
                }
                else {
                    throw new RuntimeException("No such method found: " + message);
                }
            }
        }
        catch (Exception e) {
            StringBuffer sb = new StringBuffer();
            // Build a helpful message to debug reflection issues.
            try {
                sb.append("ERROR: Could not send message '" + message + "' to target of type " + target.getClass().toString() + " \n");

                sb.append("\ttarget implements : \n");
                Class[] interfaces = target.getClass().getInterfaces();
                for (int j = 0; j < interfaces.length; j++) {
                    sb.append("\t\t" + interfaces[j].getName() + "\n");
                }
                sb.append("\n");

                sb.append("\ttarget methods: \n");
                Method[] methods = target.getClass().getMethods();
                for (int j = 0; j < methods.length; j++) {
                    sb.append("\t\t" + methods[j].getName() + "\n");
                }
                sb.append("\n");

                if (args != null) {
                    sb.append("\tArgument types: \n");
                    for (int j = 0; j < args.length; j++) {
                        sb.append("\t\t" + args[j].getClass().getName() + "\n");
                    }
                }
            }
            catch (Exception e2) {
                throw new RuntimeException("ERROR: Could not create detailed error message for failed sendMessage() call.");
            }
            throw new RuntimeException(sb.toString());
        }

    }
}
