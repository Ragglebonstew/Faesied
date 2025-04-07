# HalfDream

The Half Dream mod allows players and other entities to interact with the seemingly invisible dream world. In it, invisible monsters and structures hide from the waking world. Crossing through the veil is the only way to reveal what is hidden. But be warned, you too (and anything you build) will be imperceptible to the waking world.

## Changelog

0.7.0

	Added Bunny Plush
	Added Interloper Portal Block
	Added Interloper Portal Structure
	Removed deprecated dream block items
	
    Fixed bug where entities could hit other dream state entities
	Fixed dream blocks not replacing properly
	Fixed lighting not updating on world
	Fixed bug where structure nbt was misnamed
    Fixed crash with IPortal structure json
	Fixed IPortal spawning in oceans
	
	Implemented Player Dream component

0.6.0

	Added ability to place any block as a dream block while in dream state
	Added dreamstate command to allow setting one's dream state
	Added dreamclear command to clear all dream block and dream air refrences in a chunk
	Deprecated old dream blocks
	Removed dream entity interfaces 

	Fixed a bug where clicking a block added it to dream air
	Fixed bug where dream block reference overrided by state change
	Fixed blocklight not passing through dream blocks
	Fixed skylight not passing through dream blocks
	Fixed dropped items not matching dream state
	Fixed skylight showing in dream
	Fixed a bug where dream blocks and real blocks couldn't replae each other
	Fixed bug where dream blocks didn't drop dream items
	Fixed bug where dream chests didn't drop dream items
	Fixed bug where skel-horse would call fog server side
	Fixed bug where server skel-horse imported client player
	Fixed bugs where utility class imported client classes on server

0.5.0

    added dream chunk api for infinite layers!

    fixed bug with client side chunk sync packet
    found some god-awful bug with getOpacity in abstract block mixin

    added the ability for blocks to become dream, similar to old dream blocks
    added cbsi item for debug. Makes a ding sound when a block is either dream or disturbed
    added dream powder item which makes a block dream (at least until I can fix the block mixin bug)

    fixed bug where dream blocks replaced improperly with already replaceable blocks
    fixed bug where replacing replaceable block with dream block crashed the game
    optimized chunk sync packets
    fixed bug where client side entities all used sequence dream state
    fixed bug where entities were suffocating in dream and disturbed blocks
    fixed bug where in-house dream blocks didn't default to dream blocks

    made in-house dream blocks copy dream state of player if applicable

    fixed in-house dream block settings
    chunk sync optimization

0.4.3

    added texture for dream resin

    fixed bug where resin spawned off center of disturbed block
    fixed bug where other players would ignore player state
    fixed bug where sodium ignored dream light levels

0.4.2

    fixed dream entity component to use entity.class instead of livingentity.class to resolve item problems
    fixed all the nonsense resulting from previous fix
    fixed bug where items would pickup in wrong state
    fixed bug where dream resin dropped in real world, not dream world
    fixed bug where client would ignore sequence state

    added /setdream command to toggle dream state for any entity

    fixed bug where items would ignore player state when dropped
    fixed bug where /setdream command didn't require state value
    deprecated all isDream checks
    fixed bug where dream blocks didn't render in dual state
    fixed bug where disturbed blocks didn't de-render in dual state

0.4.1

    Added compatibility with Sodium rendering by adding WorldSlice mixin

    fixed dream block collision

    began fiddling with item groups
    added isDream check for items

    fixed abstract block collision that ignored items

