package org.genshin.old.scrollninja.object;

public interface StageBase {

	// 更新関数
	public void Update(float deltaTime);

	// 描画関数
	public void Draw();

	// 初期化関数
	public void Init();

	// 開放関数
	public void Release();

	public int GetStageNum();

}
