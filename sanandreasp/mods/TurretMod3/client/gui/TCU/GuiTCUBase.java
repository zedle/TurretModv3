package sanandreasp.mods.TurretMod3.client.gui.TCU;

import sanandreasp.mods.TurretMod3.client.gui.GuiItemTab;
import sanandreasp.mods.TurretMod3.entity.turret.EntityTurret_Base;
import sanandreasp.mods.TurretMod3.registry.TM3ModRegistry;
import sanandreasp.mods.managers.SAP_LanguageManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GuiTCUBase extends GuiScreen {
	protected int guiLeft;
	protected int guiTop;
	protected int xSize;
	protected int ySize;
	
    protected GuiButton tabTurretInfo;
    protected GuiButton tabTurretTargets;
    protected GuiButton tabTurretSettings;
    protected GuiButton tabTurretUpgrades;
	
	public EntityTurret_Base turret;
	
	protected static SAP_LanguageManager langman = TM3ModRegistry.manHelper.getLangMan();
	
	public GuiTCUBase() {
		this.allowUserInput = true;
	}
	
	@Override
	public void initGui() {
		this.xSize = 176;
		this.ySize = 222;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
        tabTurretInfo = new GuiItemTab(buttonList.size(), this.guiLeft - 23, this.guiTop + 10, new ItemStack(Item.sign), langman.getTranslated("turretmod3.gui.tcu.btninfo"), false);
        buttonList.add(tabTurretInfo);
        if (this instanceof GuiTCUInfo) tabTurretInfo.enabled = false;
        tabTurretTargets = new GuiItemTab(buttonList.size(), this.guiLeft - 23, this.guiTop + 36, new ItemStack(Item.swordDiamond), langman.getTranslated("turretmod3.gui.tcu.btntarg"), false);
        buttonList.add(tabTurretTargets);
        if (this instanceof GuiTCUTargets) tabTurretTargets.enabled = false;
        tabTurretSettings = new GuiItemTab(buttonList.size(), this.guiLeft - 23, this.guiTop + 62, new ItemStack(Item.writableBook), langman.getTranslated("turretmod3.gui.tcu.btnsetg"), false);
        buttonList.add(tabTurretSettings);
        if (this instanceof GuiTCUSettings) tabTurretSettings.enabled = false;
        tabTurretUpgrades = new GuiItemTab(buttonList.size(), this.guiLeft - 23, this.guiTop + 88, new ItemStack(Item.saddle), langman.getTranslated("turretmod3.gui.tinfo.btnupgd"), false);
        buttonList.add(tabTurretUpgrades);
        if (this instanceof GuiTCUUpgrades) tabTurretUpgrades.enabled = false;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		if (this.turret != null && !this.turret.hasPlayerAccess(this.mc.thePlayer)) {
			buttonList.remove(this.tabTurretSettings);
			buttonList.remove(this.tabTurretTargets);
			buttonList.remove(this.tabTurretUpgrades);
		}
		
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		GuiTCUBase gui = null;
		if (par1GuiButton.id == tabTurretInfo.id) {
			gui = new GuiTCUInfo();
		} else if (par1GuiButton.id == tabTurretTargets.id) {
			gui = new GuiTCUTargets();
		} else if (par1GuiButton.id == tabTurretSettings.id) {
			gui = new GuiTCUSettings();
		} else if (par1GuiButton.id == tabTurretUpgrades.id) {
			gui = new GuiTCUUpgrades();
		}
		
		if (gui != null) {
			gui.turret = this.turret;
			this.mc.displayGuiScreen(gui);
		}
	}

	@Override
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode)
        {
            this.mc.thePlayer.closeScreen();
        }
    }
}
