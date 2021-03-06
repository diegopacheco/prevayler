/*
 * Copyright (c) 2003 Jay Sachs. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name "Prevayler" nor the names of its contributors
 *    may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.prevayler.contrib.facade;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * A <code>Serializable</code> representation of a
 * <code>Method</code>.
 *
 * @author Jay Sachs [jay@contravariant.org]
 * @since 0_1
 */
public class MethodInfo implements Serializable {
  private static final long serialVersionUID = -7565901432660010880L;

  /**
   * Construct a <code>MethodInfo</code> from the specified
   * <code>Method</code>.
   *
   * @param p_method the <code>Method</code> to "wrap"
   */
  public MethodInfo(Method p_method) {
    m_name = p_method.getName();
    Class<?>[] paramTypes = p_method.getParameterTypes();
    m_argTypes = new String[paramTypes.length];
    for (int i = 0; i < m_argTypes.length; ++i) {
      m_argTypes[i] = paramTypes[i].getName();
    }
    m_className = p_method.getDeclaringClass().getName();
  }

  /**
   * "Reconstitute" (i.e., lookup reflectively) the
   * <code>Method</code> represented by this object. Note that
   * different results may occur if different
   * <code>ClassLoader</code>s are used during construction and the
   * invocation of this method.
   *
   * @return the <code>Method</code> represented by this object
   * @throws the usual batch of reflection-based exceptions if
   *             something goes wrong
   */
  public Method getMethod()
      throws Exception {
    Class<?>[] args = new Class<?>[m_argTypes.length];
    for (int i = 0; i < args.length; ++i) {
      args[i] = Class.forName(m_argTypes[i]);
    }
    return Class.forName(m_className).getMethod(m_name, args);
  }

  private final String m_name;
  private final String m_className;
  private final String[] m_argTypes;

}
