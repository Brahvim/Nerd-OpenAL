package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlMp3Buffer;

/**
 * @deprecated since I relied on an old library and doing that failed.
 *             ...Only if I knew how to use it correctly!
 */
@Deprecated
public class AlMp3BufferAsset extends AlBufferAsset<AlMp3Buffer> {

	public AlMp3BufferAsset(final String p_path, final boolean p_autoDispose) {
		super(p_path, p_autoDispose);
	}

	@Override
	protected AlMp3Buffer createBuffer(final NerdAl p_alMan) {
		return new AlMp3Buffer(p_alMan);
	}

}
