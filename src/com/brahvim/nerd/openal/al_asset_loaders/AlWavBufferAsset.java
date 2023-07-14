package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;

/**
 * @deprecated since the Java APIs don't function in our favor here - to make
 *             this work, I'll need a way to convert!
 */
@Deprecated
public class AlWavBufferAsset extends AlBufferAsset<AlWavBuffer> {

	public AlWavBufferAsset(final String p_path, final boolean p_autoDispose) {
		super(p_path, p_autoDispose);
	}

	@Override
	protected AlWavBuffer createBuffer(final NerdAl p_alMan) {
		return new AlWavBuffer(p_alMan);
	}

}
