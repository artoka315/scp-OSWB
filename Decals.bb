;[Block]

Const MAX_DECALS% = 21

;Decal IDs
Const DECAL_GLITCH_1% = 0
Const DECAL_GLITCH_2% = 1

Const DECAL_BLOOD_1% = 2
Const DECAL_BLOOD_2% = 3
Const DECAL_BLOOD_3% = 4
Const DECAL_BLOOD_4% = 5
Const DECAL_BLOOD_5% = 6

Const DECAL_PAPER_STRIPS% = 7

Const DECAL_PD_1% = 8
Const DECAL_PD_2% = 9
Const DECAL_PD_3% = 10
Const DECAL_PD_4% = 11
Const DECAL_PD_5% = 12

Const DECAL_BLOOD_DROP_1% = 13
Const DECAL_BLOOD_DROP_2% = 14

Const DECAL_BULLET_HOLE_1% = 15
Const DECAL_BULLET_HOLE_2% = 16

Const DECAL_BLOOD_6% = 17
Const DECAL_PD_6% = 18
Const DECAL_409% = 19
Const DECAL_427% = 20
;[End Block]

Type Decals
	Field obj%
	Field SizeChange#, Size#, MaxSize#
	Field AlphaChange#, Alpha#
	Field blendmode%
	Field fx%
	Field ID%
	Field Timer#
	
	Field lifetime#
	
	Field x#, y#, z#
	Field pitch#, yaw#, roll#
End Type

Function CreateDecal.Decals(id%, x#, y#, z#, pitch#, yaw#, roll#)
	Local d.Decals = New Decals
	
	d\x = x
	d\y = y
	d\z = z
	d\pitch = pitch
	d\yaw = yaw
	d\roll = roll
	
	d\MaxSize = 1.0
	
	d\Alpha = 1.0
	d\Size = 1.0
	d\obj = CreateSprite()
	d\blendmode = 1
	
	EntityTexture(d\obj, DecalTextures[id])
	EntityFX(d\obj, 0)
	SpriteViewMode(d\obj, 2)
	PositionEntity(d\obj, x, y, z)
	RotateEntity(d\obj, pitch, yaw, roll)
	
	d\ID = id
	
	If DecalTextures[id] = 0 Or d\obj = 0 Then Return Null
	
	Return d
End Function


Function LoadDecals()
	Local i%

  For i = DECAL_GLITCH_1 to DECAL_BLOOD_5
          DecalTextures[i] = LoadTexture_Strict("GFX\decal"+(i+1)+".png", 1 + 2)
      Next
          DecalTextures[DECAL_PAPER_STRIPS] = LoadTexture_Strict("GFX\items\INVpaperstrips.jpg", 1 + 2)
  For i = DECAL_PD_1 to DECAL_PD5_5
          DecalTextures[i] = LoadTexture_Strict("GFX\decalpd"+(i-7)+".png", 1 + 2)
      Next
 For i = DECAL_BULLET_HOLE_1 to DECAL_BULLET_HOLE_2
          DecalTextures[i] = LoadTexture_Strict("GFX\bullethole"+(i-13)+".jpg", 1 + 2)
      Next
 For i = DECAL_BLOOD_DROP_1 to DECAL_BLOOD_DROP_2
          DecalTextures[i] = LoadTexture_Strict("GFX\blooddrop"+(i-13)+".png", 1 + 2)
      Next
      DecalTextures[DECAL_PD_6] = LoadTexture_Strict("GFX\decalpd6.dc", 1 + 2)
      DecalTextures[DECAL_427] = LoadTexture_Strict("GFX\decal427.png", 1 + 2)
      DecalTextures[DECAL_BLOOD_6] = LoadTexture_Strict("GFX\decal8.png", 1 + 2)
      DecalTextures[DECAL_409] = LoadTexture_Strict("GFX\decal19.png", 1 + 2)

End Function

Function UpdateDecals()
	Local d.Decals
	For d.Decals = Each Decals
		If d\SizeChange <> 0 Then
			d\Size=d\Size + d\SizeChange * FPSfactor
			ScaleSprite(d\obj, d\Size, d\Size)
			
			Select d\ID
				Case DECAL_DECAY
					If d\Timer <= 0 Then
						Local angle# = Rand(360)
						Local temp# = Rnd(d\Size)
						Local d2.Decals = CreateDecal(DECAL_CRACKS, EntityX(d\obj) + Cos(angle) * temp, EntityY(d\obj) - 0.0005, EntityZ(d\obj) + Sin(angle) * temp, EntityPitch(d\obj), Rnd(360), EntityRoll(d\obj))
						d2\Size = Rnd(0.1, 0.5) : ScaleSprite(d2\obj, d2\Size, d2\Size)
						PlaySound2(DecaySFX[Rand(1, 3)], Camera, d2\obj, 10.0, Rnd(0.1, 0.5))
						;d\Timer = d\Timer + Rand(50,150)
						d\Timer = Rand(50, 100)
					Else
						d\Timer= d\Timer-FPSfactor
					End If
				;Case 6
				;	EntityBlend d\obj, 2
			End Select
			
			If d\Size >= d\MaxSize Then d\SizeChange = 0 : d\Size = d\MaxSize
		End If
		
		If d\AlphaChange <> 0 Then
			d\Alpha = Min(d\Alpha + FPSfactor * d\AlphaChange, 1.0)
			EntityAlpha(d\obj, d\Alpha)
		End If
		
		If d\lifetime > 0 Then
			d\lifetime=Max(d\lifetime-FPSfactor,5)
		EndIf
		
		If d\Size <= 0 Lor d\Alpha <= 0 Lor d\lifetime=5.0  Then
			d\obj = FreeEntity_Strict(d\obj)
			Delete d
		End If
	Next
End Function
  
