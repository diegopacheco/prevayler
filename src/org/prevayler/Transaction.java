// Prevayler(TM) - The Open-Source Prevalence Layer.
// Copyright (C) 2001-2003 Klaus Wuestefeld.
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License version 2.1 as published by the Free Software Foundation. This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.

package org.prevayler;


/**
 * An atomic transaction to be executed on a prevalent system. Any operation which changes the observable state of a prevalent system must be encapsulated as a Transaction.
 */
public interface Transaction extends java.io.Serializable {

	/**
	 * This method is called by Prevayler.execute(Transaction) to execute this transaction on the given prevalent system. See org.prevayler.demos for usage examples.
	 */
	public void executeOn(Object prevalentSystem);

}
