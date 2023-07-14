package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;

public class AlOggBufferAsset extends AlBufferAsset<AlOggBuffer> {

	public AlOggBufferAsset(final String p_path, final boolean p_autoDispose) {
		super(p_path, p_autoDispose);
	}

	@Override
	protected AlOggBuffer createBuffer(final NerdAl p_alMan) {
		return new AlOggBuffer(p_alMan);
	}

}
