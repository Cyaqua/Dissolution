package ladysnake.dissolution.common.items;

import ladysnake.dissolution.common.Dissolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class ItemBaseResource extends Item {

    public static final List<String> variants = new ArrayList<>();

    static {
        variants.add("ectoplasm");
        variants.add("ectoplasma");
        variants.add("sulfur");
        variants.add("cinnabar");
        variants.add("mercury");
    }

    public ItemBaseResource() {
        super();
        this.setCreativeTab(Dissolution.CREATIVE_TAB);
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (int i = 0; i < variants.size(); ++i) {
            subItems.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.getMetadata() < variants.size())
            return "item." + variants.get(stack.getMetadata());
        return super.getUnlocalizedName(stack);
    }

    public static ItemStack resourceFromName(String name) {
        return resourceFromName(name, 1);
    }

    public static ItemStack resourceFromName(String name, int quantity) {
        return null;
    }

}
