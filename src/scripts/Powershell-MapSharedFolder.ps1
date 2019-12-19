clear-host

#-- INITIAL PROMPTS

    #-- ENTER START IP
        $strComputer = Read-Host -Prompt 'Hostname/IP '
        $strFolderName = Read-Host -Prompt 'Shared folder name '
        $strPath = "\\$strComputer\$strFolderName"
        $strUsername = Read-Host -Prompt 'Username '
        $strShareName = Get-Random

$cred = Get-Credential -Credential $strComputer\$strUsername
New-PSDrive -Name $strShareName -PSProvider FileSystem -Root $strPath -Credential $cred


Invoke-Item $strPath