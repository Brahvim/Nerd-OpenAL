package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdAlUpdater;
import com.brahvim.nerd.openal.al_buffers.AlMp3Buffer;

/**
 * @deprecated since I relied on an old library and doing that failed.
 *             ...Only if I knew how to use it correctly!
 */
@Deprecated
public class AlMp3BufferAsset extends AlBufferAsset<AlMp3Buffer> {

	/**
	 * {@inheritDoc}
	 */
	public AlMp3BufferAsset(final NerdAlUpdater p_alMan, final String p_path) {
		super(p_alMan, p_path);
	}

	/**
	 * {@inheritDoc}
	 */
	public AlMp3BufferAsset(final NerdAlUpdater p_alMan, final String p_path, final boolean p_autoDispose) {
		super(p_alMan, p_path, p_autoDispose);
	}

	@Override
	protected AlMp3Buffer createBuffer(final NerdAl p_alMan) {
		return new AlMp3Buffer(p_alMan);
	}

}
