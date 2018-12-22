# $language = "VBScript"
# $interface = "1.0"

crt.Screen.Synchronous = True
crt.Screen.IgnoreEscape = True

Sub Main()
    '--- check for active session
    If Not crt.Session.Connected Then
        crt.Dialog.MessageBox "Sending data requires an active connection."
        Exit Sub
    End If

    '--- check for anything in clipboard
    If Trim(crt.Clipboard.Text) = "" Then
        crt.Dialog.MessageBox "No text found in the Windows Clipboard."
        Exit Sub
    End If

    '--- start timer to display when finished
    nStartTime = Timer

    '--- Multiple lines in the Windows Clipboard are typically stored with a CRLF
    '--- separator, but this script example tries to accommodate other line endings
    '--- that might be supported by some editors. Break up lines into an array
    '--- (each line as an element within the array).
    If Instr(crt.Clipboard.Text, vbcrlf) > 0 Then
        vLines = Split(crt.Clipboard.Text, vbcrlf)
    ElseIf Instr(crt.Clipboard.Text, vblf) > 0 Then
        vLines = Split(crt.Clipboard.Text, vblf)
    Else
        vLines = Split(crt.Clipboard.Text, vbcr)
    End If

    nLineNumber = 0
    For Each strLine In vLines
        '--- Send the next line to the remote
        crt.Screen.Send strLine & vbcr

        '--- This didn't seem to work well - checking for only last 10 of string, not sure why
        'If Len(strLine) > 30 Then
            'Dim strLineLastTen
            'strLineLastTen = Right(strLine, 10)
            'nResult = crt.Screen.WaitForString(strLineLastTen, 3)
            'crt.Dialog.MessageBox strLineLastTen

        '--- Check for )# and $, If not found then check for the whole line
        vWaitFors = Array(")#", "$")
        If Not crt.Screen.WaitForStrings(vWaitFors, 3) Then

        '--- Wait for the remote to echo the line back to SecureCRT; bail if the
        '--- remote fails to echo the line back to us within 3 seconds.
        ElseIf Not crt.Screen.WaitForString(strLine, 3) Then
            crt.Dialog.MessageBox _
                "Sent " & nLineNumber + 1 & " lines, but the last one was " & _
                "not echoed back to SecureCRT within 3 seconds." & vbcrlf & _
                vbcrlf & _
                "Abandoning paste operation."
            Exit Sub
        End If
        nLineNumber = nLineNumber + 1
    Next

    ' Inform that the data has all been sent.
    crt.Dialog.MessageBox _
        nLineNumber & " lines from the clipboard have been sent." & _
        vbcrlf & vbcrlf & _
        "Time elapsed: " & GetMinutesAndSeconds(Timer - nStartTime)
End Sub

'~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Function GetMinutesAndSeconds(nTotalSecondsElapsed)
    Dim nMinutes, nSeconds, nMSeconds
    Dim nMinutesElapsed, nSecondsValue, nSecondsElapsed

    If nTotalSecondsElapsed = 0 Then
        GetMinutesAndSeconds = "less than a millisecond."
        Exit Function
    End If

    ' convert seconds into a fractional minutes value
    nMinutesElapsed = nTotalSecondsElapsed / 60

    ' convert the decimal portion into the number of remaining seconds
    nSecondsValue = nMinutesElapsed - Fix(nMinutesElapsed)
    nSecondsElapsed = Fix(nSecondsValue * 60)

    ' Remove the decimal from the minutes value
    nMinutesElapsed = Fix(nMinutesElapsed)

    ' Get the number of Milliseconds, Seconds, and Minutes to return to the
    ' caller byref
    nMSeconds = fix(1000 * (nTotalSecondsElapsed - Fix(nTotalSecondsElapsed)))
    nSeconds = nSecondsElapsed
    nMinutes = nMinutesElapsed

    ' Form the final string to be returned
    GetMinutesAndSeconds = nMinutesElapsed & " minutes, " & _
        nSecondsElapsed & " seconds, and " & _
        nMSeconds & " ms"
End Function
