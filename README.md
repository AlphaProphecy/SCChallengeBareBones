# SCChallengeBareBones
SpaceCadets Challenge 2
A barebones interpreter that accepts a language in the following form:
```
clear X; /// sets the variable X to 0
incr Y; /// Increases the value of Y by 1
decr Y; /// Decrease the vlaue of Y by 1
while X not 0 do; /// Loops over the indee
  *indented code*
end; /// end of the looped code
X = X - 15.1512 * Z / Y; /// X is set to the value of the full calculation
```
