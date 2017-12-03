![NoWorldSave logo](https://i.imgur.com/BfIgaX5.jpg)

[![Build Status](https://travis-ci.com/hugmanrique/NoWorldSave.svg?token=WSd57ScU7yMVWfFYfuGZ&branch=master)](https://travis-ci.com/hugmanrique/NoWorldSave)
[![License](https://img.shields.io/github/license/hugmanrique/NoWorldSave.svg)](LICENSE)

Disable Minecraft's server chunk saving code by replacing default methods on the fly while the server is initializing.

We all know Bukkit's `autosave: -1` option doesn't work well in multiworld environments. This plugin is extremely useful for multiarena gamemodes where the map needs to be restored to the original state after a game has ended. What most plugins do is to paste a schematic or load block raw data into the NMS Chunks. As Minecraft works on one thread, this causes a lot of lag, especially on larger servers. This plugin takes a completely different approach: it removes the NMS code in charge of saving the chunks to file. This way, **your users won't feel any lag** when an arena is restored, as nothing "really" happens.

In case you want the default behavior back, there's a config flag for restoring the original NMS code. Just set the `restore` config variable to `true`, run the server once and remove the plugin.

[![Plugin preview](https://img.youtube.com/vi/0IWI8tHRdFk/0.jpg)](https://www.youtube.com/watch?v=0IWI8tHRdFk)

In order to restore all the placed/destroyed/changed blocks on a chunk, you will need to unload it. This happens automatically when no players are near it, but it can also be manually unloaded by using the Bukkit API.

## Installation

Simply drop this plugin into your plugins directory. That's it!

## License

NoWorldSave is licensed under the [MIT License](LICENSE).