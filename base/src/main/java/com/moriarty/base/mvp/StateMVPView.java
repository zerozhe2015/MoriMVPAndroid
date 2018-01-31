package com.moriarty.base.mvp;


import com.moriarty.base.ui.interfaces.StateView;

public interface StateMVPView<T extends BasePresenter> extends BaseMVPView<T>, StateView {
}
