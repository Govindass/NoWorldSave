![NoWorldSave logo](https://i.imgur.com/BfIgaX5.jpg)

Disable Minecraft's server chunk saving code by replacing default methods on the fly while the server is initializing.

We all know Bukkit's `autosave: -1` option doesn't really work for multiworld environments. This plugin is extremely useful for multiarena gamemodes where the map needs to be restored to the original state after a game has ended. 
What most plugins do is to paste a schematic or load block raw data into the NMS Chunks. As Minecraft works on one thread, this causes a lot of lag, especially on larger servers. This plugin takes a completely different approach: it removes the NMS code in charge of saving the chunks to file. This way, your users won't feel any lag when an arena is restored, as nothing "really" happens.

In case you want the default behavior back, there's a config flag for restoring the original NMS code. Just set the `restore` config variable to `true`, run the server once and remove the plugin.



## Support

Having issues while installing or using the plugin? All my means of contact are available on [hugmanrique.me/contact/](https://hugmanrique.me/contact/). Please note that I'm not that active on the Spigot forums, so a direct message reply could take me some days to reply.

## Terms of use

Please note that by buying this resource, you agree to the following:

- You are not allowed to distribute this plugin. This means this resource can only be used on one network under the same name.
- You are not allowed to decompile the plugin without my proper permission.
- I have the right to change the price at my own discretion.
- You are not allowed to create a chargeback, create a dispute or refund your payment on PayPal.