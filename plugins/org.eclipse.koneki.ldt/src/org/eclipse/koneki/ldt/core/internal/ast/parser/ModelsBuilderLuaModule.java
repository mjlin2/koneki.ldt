/*******************************************************************************
 * Copyright (c) 2011, 2013 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.core.internal.ast.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.koneki.ldt.core.internal.Activator;
import org.eclipse.koneki.ldt.core.internal.ast.models.APIModelFactory;
import org.eclipse.koneki.ldt.core.internal.ast.models.InternalModelFactory;
import org.eclipse.koneki.ldt.core.internal.ast.models.ModelFactory;
import org.eclipse.koneki.ldt.core.internal.ast.models.common.LuaSourceRoot;
import org.eclipse.koneki.ldt.metalua.AbstractMetaLuaModule;

import com.naef.jnlua.LuaState;

public class ModelsBuilderLuaModule extends AbstractMetaLuaModule {

	public static final String LOCAL_LIB_PATH = "/script/local";//$NON-NLS-1$
	public static final String EXTERNAL_LIB_PATH = "/script/external";//$NON-NLS-1$

	public static final String MODELS_BUILDER = "javamodelsbuilder";//$NON-NLS-1$

	public static final String INTERNAL_MODEL_BUILDER = "models/internalmodelbuilder";//$NON-NLS-1$
	public static final String INTERNAL_MODEL_BUILDER_SCRIPT = INTERNAL_MODEL_BUILDER + ".mlua";//$NON-NLS-1$

	private LuaState lua = null;

	public synchronized LuaSourceRoot buildAST(final String string) {
		if (lua == null)
			lua = loadLuaModule();

		pushLuaModule(lua);
		lua.getField(-1, "build"); //$NON-NLS-1$
		lua.pushString(string);
		lua.call(1, 1);
		LuaSourceRoot luaSourceRoot = lua.checkJavaObject(-1, LuaSourceRoot.class);
		lua.pop(2);

		// lua.close();

		return luaSourceRoot;
	}

	/**
	 * @see org.eclipse.koneki.ldt.metalua.AbstractMetaLuaModule#createLuaState()
	 */
	@Override
	protected LuaState createLuaState() {
		LuaState luaState = super.createLuaState();
		InternalModelFactory.registerInternalModelFactory(luaState);
		APIModelFactory.registerAPIModelFactory(luaState);
		ModelFactory.registerModelFactory(luaState);
		return luaState;
	}

	/**
	 * @see org.eclipse.koneki.ldt.metalua.AbstractMetaLuaModule#getMetaLuaSourcePath()
	 */
	@Override
	protected List<String> getMetaLuaSourcePaths() {
		ArrayList<String> sourcepaths = new ArrayList<String>();
		sourcepaths.add(LOCAL_LIB_PATH);
		sourcepaths.add(EXTERNAL_LIB_PATH);
		return sourcepaths;
	}

	@Override
	protected List<String> getMetaLuaFileToCompile() {
		final ArrayList<String> sourcepaths = new ArrayList<String>();
		sourcepaths.add(INTERNAL_MODEL_BUILDER_SCRIPT);
		return sourcepaths;
	}

	/**
	 * @see org.eclipse.koneki.ldt.metalua.AbstractMetaLuaModule#getPluginID()
	 */
	@Override
	protected String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	/**
	 * @see org.eclipse.koneki.ldt.metalua.AbstractMetaLuaModule#getModuleName()
	 */
	@Override
	protected String getModuleName() {
		return MODELS_BUILDER;
	}

	/**
	 * @see com.naef.jnlua.eclipse.AbstractLuaModule#getLuaSourcePaths()
	 */
	@Override
	protected List<String> getLuaSourcePaths() {
		final ArrayList<String> sourcepaths = new ArrayList<String>();
		sourcepaths.add(LOCAL_LIB_PATH);
		sourcepaths.add(EXTERNAL_LIB_PATH);
		return sourcepaths;
	}
}
