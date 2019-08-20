package svenhjol.charm.automation.feature;

import svenhjol.charm.automation.block.VariableRedstoneLampBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.AUTOMATION)
public class VariableRedstoneLamp extends Feature
{
    public static VariableRedstoneLampBlock block;

    @Override
    public void init()
    {
        block = new VariableRedstoneLampBlock();
    }
}
