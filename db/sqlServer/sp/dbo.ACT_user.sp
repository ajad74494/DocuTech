/****** Object:  StoredProcedure [dbo].[ACT_user]    Script Date: 18/12/2019 12:28:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	CREATE PROC [dbo].[ACT_user]

	 @tx_action_name		varchar(32)		= NULL	OUTPUT

	, @is_active			int						= 1
	, @id_env_key			int						= NULL	OUTPUT
	, @id_user_mod_key 		int						= NULL	OUTPUT
	, @dtt_mod				datetime				= NULL	OUTPUT

	, @id_state_key			int						= NULL	OUTPUT
	, @tx_state_name		varchar(64)	= NULL	OUTPUT
	, @id_action_key		int						= NULL  OUTPUT

	, @id_event_key			int						= NULL	OUTPUT
	, @id_event_map1_key	int						= NULL	OUTPUT
	, @id_event_map2_key	int						= NULL	OUTPUT
	, @id_event_map3_key	int						= NULL	OUTPUT
	, @id_event_map4_key	int						= NULL	OUTPUT

	, @dtt_valid_from		datetime				= NULL
	, @dtt_valid_to			datetime				= NULL
	, @dtt_as_at			datetime				= NULL
	, @dtt_last_refresh		datetime				= NULL
	, @id_user_key				int								= NULL	OUTPUT
	, @id_user_ver					int								= NULL	OUTPUT
	, @tx_first_name			varchar(64)			= NULL
	, @tx_last_name				varchar(64)			= NULL
	, @tx_login_name			varchar(32)			= NULL
	, @tx_password				varchar(8000)			= NULL
	, @tx_new_password			varchar(8000)			= NULL
	, @tx_user_alias			varchar(32)			= NULL
	, @is_disabled				int								= NULL
	, @is_allow_login 			int 							= NULL
	, @is_db_authenticated		int								= NULL	OUTPUT
	, @id_created_by_key		int								= NULL	

	, @id_generic_map_key		int								= NULL	OUTPUT
	, @id_generic_map_ver		int								= NULL	OUTPUT
	, @id_region_key			int 							= NULL
	, @id_group_key				int								= NULL
	, @id_legal_entity_key		int 							= NULL
	, @id_group_ver				int								= NULL	OUTPUT
	, @tx_group_name			varchar(64)			= NULL

	, @is_primary				int								= NULL
	, @ct_sort_order			int								= NULL

	, @id_login_key				int								= NULL	OUTPUT
	, @id_login_ver				int								= NULL	OUTPUT
	, @dtt_login				datetime						= NULL
	, @dtt_logout				datetime						= NULL
	, @tx_client_ip_addr		varchar(16)				= NULL
	, @tx_desc					varchar(4096)				= NULL
	, @tx_authentication_code 	varchar(8) 				 		= NULL 
	, @is_logged_in  			int 							= NULL
	, @tx_email 				varchar(64) 				 	= NULL 

	, @is_first_login			int 							= NULL


	, @is_sel_data			int						= NULL
	, @tx_log_level			varchar(32)			= NULL
	, @id_log_level			int						= NULL
	, @is_record_time		bit						= NULL
	, @is_print_msg			bit						= NULL
	, @is_persist_msg		bit						= NULL
	, @tx_json_log_msg		varchar(MAX)	= NULL		OUTPUT
AS
BEGIN
	-- GLOBAL VARS --
	DECLARE	  @g_tx_action_name_orig	varchar(32)
			, @g_tx_state_name_orig		varchar(64)

			, @g_id_env_key				int
			, @g_tx_env_name			varchar(256)

			, @g_ct_row					int
			, @g_id_return_status		int

			, @g_id_error_key			int
			, @g_tx_err_msg				varchar(1024)
			, @g_tx_err_msg_tmp			varchar(1024)
			, @g_is_outer_sp			bit

	DECLARE   @g_dt_sys_today			date
			, @g_dt_sys_prev_day		date
			, @g_dt_sys_next_day		date
			, @g_dt_sys_prev_biz_day	date
			, @g_dt_sys_next_biz_day	date

	

	--	DECLARE @g_in_tran		int
	DECLARE	@g_is_ext_txn	bit
	DECLARE @g_is_sp_txn	bit

	SELECT @g_is_ext_txn = 0, @g_is_sp_txn = 0

	DECLARE @g_tmp_int		int

	DECLARE   @g_dtt_log			datetime
	 		, @g_tx_tmp_log_msg		varchar(MAX)
	 		, @g_tx_log_msg			varchar(MAX)

		  	, @g_id_err_num			int
			, @g_id_err_sev			int
			, @g_id_err_state		int
			, @g_tx_sp_name			varchar(128)
			, @g_id_line_num		int
			, @g_id_log_level		int

			, @g_is_record_time		bit
			, @g_is_print_msg		bit
			, @g_is_persist_msg  	bit

			, @g_tx_json_log_msg	varchar(MAX)

	SELECT	@g_tx_json_log_msg = ''

	DECLARE @g_dtt_proc_start		datetime
	DECLARE @g_dtt_proc_end			datetime
	DECLARE @g_dtt_tot_elapsed		int

	DECLARE	@g_dtt_query_start		datetime
	DECLARE	@g_dtt_query_end		datetime
	DECLARE @g_dtt_query_elapsed	int

	DECLARE @g_tmp_tx_tot_time		varchar(255)
	DECLARE @g_tmp_tx_query_time	varchar(255)

	IF ( (@@NESTLEVEL = 1) )
	BEGIN
		IF ( ((@@OPTIONS & 2) != 2) AND (@@TRANCOUNT = 0) )
		BEGIN
			SELECT @g_is_ext_txn = 0
		END
		ELSE
		BEGIN
			SELECT @g_is_ext_txn = 1
		END
	END

	IF (((@tx_log_level IS NULL) OR (@tx_log_level = '?')) OR ((@is_persist_msg IS NULL) OR (@is_persist_msg = -2147483648)) OR ((@is_record_time IS NULL) OR (@is_record_time = -2147483648)))
	BEGIN
		EXEC @g_id_return_status = GET_sp_log_level @tx_sp_name='ACT_user', @tx_log_level=@tx_log_level OUTPUT, @id_log_level=@id_log_level OUTPUT, @is_record_time=@is_record_time OUTPUT, @is_print_msg=@is_print_msg OUTPUT, @is_persist_msg=@is_persist_msg OUTPUT
	END

	SELECT @g_is_record_time = @is_record_time, @g_is_print_msg = @is_print_msg, @g_is_persist_msg	= @is_persist_msg, @g_id_log_level = @id_log_level

	SELECT @g_tx_err_msg = 'Entering[ACT_user] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'ACT_user.sp', @g_id_line_num = 166

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	EXEC @g_id_return_status = GET_environment @id_env_key = @g_id_env_key OUTPUT, @tx_env_name	= @g_tx_env_name OUTPUT, @id_user_key = @id_user_mod_key

	SELECT @g_tx_err_msg = 'Error calling SP -> [GET_ENVIRONMENT] '

	IF (@g_id_return_status != 0)
	BEGIN
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30001 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30001
	END

	IF (((@dtt_mod IS NULL) OR (@dtt_mod = '01/01/1970')))
	BEGIN
		SELECT @dtt_mod = GETDATE()
	END

	IF (@tx_action_name != 'LOGIN')
	BEGIN
		IF (((@id_user_mod_key IS NULL) OR (@id_user_mod_key = -2147483648)))
		BEGIN
			RAISERROR ('Error:Int [@id_user_mod_key] should not be NULL', 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30011 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30011
		END
	END

	-- X_SET_NULL_ACTION

	SELECT  @g_tx_action_name_orig = @tx_action_name

	DECLARE	  @l_is_db_auth 			int
			, @l_is_ip_auth				int
			, @l_is_allow_login 		int
			, @l_max_concurrent_user 	int
			, @l_concurrent_user 		int
			, @l_group_key 				int

	------------------
	--- Validation ---
	------------------
	DECLARE @l_tx_password varchar(8000)

	SELECT @g_tx_err_msg = 'ACTION : ' + @tx_action_name
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'ACT_user.sp', @g_id_line_num = 332

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	IF @tx_action_name NOT IN ('LOGOUT_ALL_USER','NEW', 'UPDATE', 'DELETE', 'CHANGE_PWD', 'LOGIN', 'LOGOUT', 'MAP_USER_TO_GROUP', 'UNMAP_USER_TO_GROUP', 'AUDIT_HISTORY', 'AUTHENTICATE_LOGIN', 'SELECT_AT_LOGIN', 'LOGGEDIN_USER_APP' )
	BEGIN
		SELECT @g_tx_err_msg = 'Invalid Action -> ' + ISNULL(@tx_action_name, '?')
        RAISERROR (@g_tx_err_msg, 16, 1)
	IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
	IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
	BEGIN
		-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'

		IF (30000 = 0)
		BEGIN
			--RAISERROR (@tx_json_log_msg, 10, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10

		END
		ELSE
		BEGIN
			--RAISERROR (@tx_json_log_msg, 16, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
		END
	END

	RETURN 30000
	END

	--Logged in status of a user[Imamul Hossain]
	IF(@tx_action_name = 'LOGGEDIN_USER_APP')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_USER'
		, U.id_user_key
		, U.tx_login_name
		--, L.tx_app_name
		, L.dtt_login 
		, L.tx_client_ip_addr
		, L.id_login_key
		FROM T_USER U
		JOIN T_LOGIN L ON L.id_user_key = U.id_user_key
		WHERE U.id_user_key = ISNULL(@id_user_key, U.id_user_key)
		AND L.is_logged_in = 1
	END

	IF (@tx_action_name = 'LOGOUT_ALL_USER')
	BEGIN
		UPDATE T_LOGIN SET is_logged_in = 0
		UPDATE T_LOGIN_AUDIT SET is_logged_in = 0
	END

	IF  (@tx_action_name = 'LOGIN')
	BEGIN
		IF (((@tx_login_name IS NULL) OR (@tx_login_name = '?')))
		BEGIN
			RAISERROR ('Error: String [@tx_login_name] should not be NULL', 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30009 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30009
		END

		-- VALIDATE LOGIN


		SELECT	  @id_env_key	= USR.id_env_key
				, @id_user_key	= USR.id_user_key
		FROM	T_USER			USR
		WHERE	USR.id_user_key				= ISNULL(@id_user_key			, USR.id_user_key)
		AND		LOWER(USR.tx_login_name)	= LOWER(ISNULL(@tx_login_name	, USR.tx_login_name))
		AND		USR.is_active				= 1

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Invalid User ' + @tx_login_name
		IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		-- Check if login is allowed or not----
		SELECT  @l_is_allow_login = is_allow_login
		FROM 	T_USER 	USR
		WHERE	USR.id_user_key				= ISNULL(@id_user_key			, USR.id_user_key)
		AND		LOWER(USR.tx_login_name)	= LOWER(ISNULL(@tx_login_name	, USR.tx_login_name))
		AND		USR.is_active				= 1

		IF(@l_is_allow_login = 0)
		BEGIN
			SELECT	  @g_tx_err_msg 		= 'User is not allowed for login' + @tx_login_name
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END

		UPDATE 	T_USER 
		SET 	is_first_login 	= 1
		FROM 	T_USER USR
		JOIN 	T_PASSWORD_LOG LOG ON LOG.id_user_key = USR.id_user_key
		WHERE 	USR.id_user_key 	= @id_user_key
		AND 	DATEDIFF(day,LOG.dtt_mod,GETDATE()) >30
		AND		LOG.dtt_mod = (SELECT MAX(dtt_mod) from T_PASSWORD_LOG where id_user_key 	= @id_user_key )


		SELECT @l_is_db_auth = tx_pref_value FROM T_PREFERENCE WHERE tx_pref_name = 'IS_DB_AUTH'

		IF( @l_is_db_auth = 1 )
		BEGIN
			-- CHECK VALID USER WITH PASSWORD
			IF (((@tx_password IS NULL) OR (@tx_password = '?')))
			BEGIN
				RAISERROR ('Error: String [@tx_password] should not be NULL', 16, 1)
				IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30009 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30009
			END
			SELECT	@l_tx_password = CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, id_user_key), tx_password))
			FROM	T_USER	USR
			WHERE	USR.id_user_key	= @id_user_key

			IF (@l_tx_password COLLATE Latin1_General_CS_AS != @tx_password COLLATE Latin1_General_CS_AS OR @l_tx_password IS NULL )
			BEGIN
				SELECT	  @g_tx_err_msg 		= 'Invalid Password for' + @tx_login_name
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999
			END
			ELSE
			BEGIN
				SELECT 	@is_db_authenticated = 1
			END
			
		END

		EXEC @g_id_return_status = GET_preference
								  @id_user_key 		= @id_user_key
								, @tx_pref_name	 	= 'IS_IP_AUTH'
								, @tx_pref_value 	= @l_is_ip_auth 				OUTPUT

		SELECT @g_tx_err_msg = 'Error calling SP -> [GET_preference] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END

		-- SELECT @l_is_ip_auth = tx_pref_value FROM T_PREFERENCE WHERE tx_pref_name = 'IS_IP_AUTH'

		IF( @l_is_ip_auth = 1 )
		BEGIN
			-- CHECK VALID USER WITH  IP
			SELECT 	@l_group_key = GRP.id_group_key
			FROM 	T_USER 			USR
			JOIN	V_USER_GROUP	GRP 	ON GRP.id_user_key = USR.id_user_key
			JOIN 	T_IP_WHITELIST 	IP 		ON IP.id_group_key = GRP.id_group_key
			WHERE 	USR.id_user_key 	= @id_user_key
			AND 	IP.tx_ip_address 	= @tx_client_ip_addr

			SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
			
			IF( @g_ct_row < 1)
			BEGIN
				SELECT @g_tx_err_msg = 'Invalid IP ' + @tx_login_name
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999
			END

			-- CHECK MAAXIMUM CONCURRENT USER

			

			EXEC @g_id_return_status = GET_preference
								  @id_user_key 		= @id_user_key
								, @tx_pref_name	 	= 'MAX_CONCURRENT_USER'
								, @tx_pref_value 	= @l_max_concurrent_user 			OUTPUT

			SELECT 	@l_concurrent_user = COUNT(*)
			FROM  	V_USER_GROUP 	GRP
			JOIN 	T_LOGIN 		LOGIN 	ON LOGIN.id_user_key = GRP.id_user_key
			WHERE 	GRP.id_group_key 			= @l_group_key
			AND 	LOGIN.is_logged_in			= 1
			AND 	LOGIN.tx_client_ip_addr 	= @tx_client_ip_addr

			IF( @l_concurrent_user >= @l_max_concurrent_user )
			BEGIN
				SELECT @g_tx_err_msg = 'Exceeded Maximum Number of Concurrent User'
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999

			END

		END

		
		---CHECKING IP FOR USER:PURPOSE:[one user can login from only one ip]

		DECLARE @l_tx_client_ip_addr		varchar(16)
			  , @l_is_preserve_single_login int

		EXEC @g_id_return_status = GET_preference
							  @id_user_key 		= @id_user_key
							, @tx_pref_name	 	= 'PRESERVE_SINGLE_LOGIN'
							, @tx_pref_value 	= @l_is_preserve_single_login 			OUTPUT

		IF(@l_is_preserve_single_login = 1)
		BEGIN
			SELECT 	@l_tx_client_ip_addr = tx_client_ip_addr
			FROM 	T_LOGIN 		LOGIN
			WHERE 	LOGIN.is_logged_in			= 1
			AND 	LOGIN.id_user_key 			= @id_user_key

			IF( @l_tx_client_ip_addr != @tx_client_ip_addr)
			BEGIN
				SELECT @g_tx_err_msg = 'You have already logged in from another computer.'
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999

			END
		END


		-- NOW return the full set of Info
		EXEC @g_id_return_status = SEL_user
									  @tx_action_name		= @tx_action_name
									, @is_active			= @is_active
									, @id_env_key			= @id_env_key
									, @dtt_mod				= @dtt_mod
									, @id_state_key			= @id_state_key
									, @id_action_key		= @id_action_key
									, @dtt_valid_from		= @dtt_valid_from
									, @dtt_valid_to			= @dtt_valid_to
									, @dtt_as_at			= @dtt_as_at
								
									, @tx_log_level			= @tx_log_level
									, @id_log_level			= @id_log_level
									, @is_record_time		= @is_record_time
								 	, @is_print_msg  		= @is_print_msg
								 	, @is_persist_msg  		= @is_persist_msg
									, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_user_key		= @id_user_key
									, @tx_login_name	= @tx_login_name
									, @tx_password		= @tx_password
									, @is_full_details	= 1

		SELECT @g_tx_err_msg = 'Error calling SP -> [SEL_user] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END

	END


	IF ( ((@id_event_key IS NULL) OR (@id_event_key = -2147483648)) )
	BEGIN
		SELECT @g_is_outer_sp	= 1, @id_event_map1_key = 0, @id_event_map2_key = 0, @id_event_map3_key = 0, @id_event_map4_key = 0

		EXEC @g_id_return_status = GET_system_key @id_env_key	= @g_id_env_key, @tx_key_name = 'id_event_key', @id_key_value	= @id_event_key OUTPUT, @num_keys = 1
	
		SELECT @g_tx_err_msg = 'Error generating key for id_event_key'
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30003 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30003
		END
	END

	IF  (@tx_action_name IN ( 'LOGIN', 'LOGOUT', 'AUTHENTICATE_LOGIN'))
	BEGIN
		IF (((@id_login_ver IS NULL) OR (@id_login_ver = -2147483648)))
		BEGIN
			SELECT	  @id_login_key = id_login_key
					, @id_login_ver = id_login_ver
			FROM	T_LOGIN
			WHERE	id_user_key = @id_user_key
			AND		is_active	= 1
		END
		IF ((@tx_action_name = 'LOGIN'))
		BEGIN
			SELECT	  @dtt_login	= GETDATE()
					, @dtt_logout	= '01/01/1970'
					, @tx_desc		= 'LOGIN'



			DECLARE @l_is_logged_in INT = 1
					, @l_tx_authentication_code varchar (8) = '?'
					,@l_is_two_factor_login INT = 0


			-- get config two factor login enable or not
			select @l_is_two_factor_login = tx_pref_value 
			from T_PREFERENCE
			where tx_pref_name = 'IS_TWO_FACTOR_LOGIN'
			
			-- CHECK IF TWO FACTOR LOGIN ENABLE
			IF(@l_is_two_factor_login IS NOT NULL AND @l_is_two_factor_login = 1)
			BEGIN
				SELECT @l_is_logged_in = 2
				--generate auth code and store it in local variable to insert it to db
				-- code is 6 char
				SELECT @l_tx_authentication_code =  SUBSTRING((SELECT CONVERT(varchar(255),NEWID())),0,7)
			END 

			-- NO entries in T_LOGIN
			IF (((@id_login_ver IS NULL) OR (@id_login_ver = -2147483648)))
			BEGIN
				SELECT	  @id_login_ver = 0

				EXEC @g_id_return_status = INS_login
											  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
											, @id_user_key				= @id_user_key      	OUTPUT
											, @dtt_login				= @dtt_login
											, @dtt_logout				= @dtt_logout
											, @tx_client_ip_addr		= @tx_client_ip_addr
											, @tx_desc					= @tx_desc
											, @is_logged_in 			= @l_is_logged_in
											, @tx_authentication_code 	= @l_tx_authentication_code
				 --PRINT 'Inside INS_LOGIN'
				SELECT @g_tx_err_msg = 'Error calling SP -> [INS_login] '
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR (@g_tx_err_msg, 16, 1)
					IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30001 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30001
				END

			END
			ELSE
			BEGIN

				EXEC @g_id_return_status = UPD_login
											  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
											, @id_login_key				= @id_login_key			OUTPUT
											, @id_login_ver				= @id_login_ver			OUTPUT
											, @id_user_key				= @id_user_key
											, @dtt_login				= @dtt_login
											, @dtt_logout				= @dtt_logout
											, @tx_client_ip_addr		= @tx_client_ip_addr
											, @tx_desc					= @tx_desc
											, @is_logged_in 			= @l_is_logged_in
											, @tx_authentication_code 	= @l_tx_authentication_code
				--PRINT 'Inside UPDATE_LOGIN'
				SELECT @g_tx_err_msg = 'Error calling SP -> [UPD_login] '
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR (@g_tx_err_msg, 16, 1)
					IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30001 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30001
				END
			END
		END
		IF ((@tx_action_name = 'AUTHENTICATE_LOGIN'))
		BEGIN
			DECLARE  
					@l_dtt_mod DATETIME = NULL
					, @l_is_update INT = 0
					, @l_login_auth_timeout INT = NULL
					, @l_login_auth_code_timeout INT = 0

			-- load login auth code timeout config
			SELECT @l_login_auth_timeout = tx_pref_value 
			from T_PREFERENCE
			WHERE tx_pref_name = 'LOGIN_AUTH_CODE_TIMEOUT'

			SELECT	 @l_tx_authentication_code	= tx_authentication_code
					, @tx_desc		= 'LOGIN'
					, @l_dtt_mod 	  =  dtt_mod
					--, @l_is_logged_in = is_logged_in
					FROM T_LOGIN
					WHERE id_user_key = @id_user_key

					-- check if authenticaiton code timeou or not
			IF( (SELECT DATEDIFF(MINUTE,@l_dtt_mod,GETDATE()))> @l_login_auth_timeout ) 
			BEGIN
				SELECT @l_is_update  = 1
			   		  ,@tx_authentication_code  ='?'
					  ,@l_is_logged_in = 0
					  , @l_login_auth_code_timeout = 1
			END
			ELSE
			BEGIN
				-- check auth code not time out and valid
				IF(@l_tx_authentication_code = @tx_authentication_code)
				BEGIN
					SELECT @l_is_update  = 1
			 			  ,@tx_authentication_code  ='?'
			 			  ,@l_is_logged_in = 1
				END
				ELSE
				BEGIN
					
					SELECT @g_tx_err_msg = 'Invalid authentication code -> ' + @tx_authentication_code
					RAISERROR (@g_tx_err_msg, 16, 1)
					IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (99999 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 99999
				END
			END
			IF (@l_is_update = 1)
			BEGIN
				EXEC @g_id_return_status = UPD_login
										  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
										, @id_login_key				= @id_login_key			OUTPUT
										, @id_login_ver				= @id_login_ver			OUTPUT
										, @id_user_key				= @id_user_key
										, @dtt_login				= @dtt_login
										, @dtt_logout				= @dtt_logout
										, @tx_client_ip_addr		= @tx_client_ip_addr
										, @tx_desc					= @tx_desc
										, @is_logged_in 			= @l_is_logged_in
										, @tx_authentication_code 	= @tx_authentication_code

			SELECT @g_tx_err_msg = 'Error calling SP -> [UPD_login] '
			IF (@g_id_return_status != 0)
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30001 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30001
			END
			END

			IF(@l_login_auth_code_timeout = 1)
			BEGIN
				SELECT @g_tx_err_msg = 'Authentication code expired.'
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999
			END

		END
		IF @tx_action_name IN ('LOGOUT')
		BEGIN
			SELECT	  @dtt_login	= '01/01/1970'
					, @dtt_logout	= GETDATE()
					, @tx_desc		= 'LOGOUT'

			EXEC @g_id_return_status = UPD_login
										  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
										, @id_login_key				= @id_login_key			OUTPUT
										, @id_login_ver				= @id_login_ver			OUTPUT
										, @id_user_key				= @id_user_key
										, @dtt_login				= @dtt_login
										, @dtt_logout				= @dtt_logout
										, @tx_client_ip_addr		= @tx_client_ip_addr
										, @tx_desc					= @tx_desc
										, @is_logged_in 			= 0
										, @tx_authentication_code 	= @l_tx_authentication_code

			SELECT @g_tx_err_msg = 'Error calling SP -> [UPD_login] '
			IF (@g_id_return_status != 0)
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30001 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30001
			END
		END
	END

	IF ((@tx_action_name = 'CHANGE_PWD'))
	BEGIN
		IF ( ((@id_user_key IS NULL) OR (@id_user_key = -2147483648)) )
		BEGIN
			IF (((@tx_login_name IS NULL) OR (@tx_login_name = '?')) )
			BEGIN
				SELECT @g_tx_err_msg = 'Either UserId or login.name MUST be supplied'
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999
			END
			ELSE
			BEGIN
				-- tx_login_name supplied
				SELECT	  @id_user_key		= USR.id_user_key
						, @id_user_ver		= USR.id_user_ver
						, @tx_login_name	= USR.tx_login_name
						, @l_tx_password	= CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, id_user_key), tx_password))
				FROM	T_USER	USR
				WHERE	USR.tx_login_name	= @tx_login_name
				AND		USR.is_active		= 1

				SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
				SELECT @g_tx_err_msg = 'Invalid User loginId' + @tx_login_name
				IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
				BEGIN
					RAISERROR (@g_tx_err_msg, 16, 1)
					IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (99999 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 99999
				END
			END
		END
		ELSE
		BEGIN
			-- id_user_key supplied

			SELECT	  @id_user_key		= USR.id_user_key
					, @id_user_ver		= USR.id_user_ver
					, @tx_login_name	= USR.tx_login_name
					, @l_tx_password	= CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, id_user_key), tx_password))
			FROM	T_USER	USR
			WHERE	USR.id_user_key		= @id_user_key
			AND		USR.is_active		= 1

			SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
			SELECT @g_tx_err_msg = 'Invalid UserKey ' + @id_user_key
			IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999
			END
		END

		-- PWD already set in DB, so confirm  old PWD
		IF (NOT ((@l_tx_password IS NULL) OR (@l_tx_password = '?')))
		BEGIN
			IF (((@tx_password IS NULL) OR (@tx_password = '?')))
			BEGIN
				RAISERROR ('Error: String [@tx_password] should not be NULL', 16, 1)
				IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30009 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30009
			END

			-- Validate OLD Password
			-- SELECT	@l_tx_password = CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, id_user_key), tx_password))
			-- FROM	T_USER	USR
			-- WHERE	USR.id_user_key	= @id_user_key

			IF (@l_tx_password != @tx_password)
			BEGIN
				SELECT @g_tx_err_msg = 'Invalid Current PWD'
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999
			END

			IF (@tx_new_password = @l_tx_password)
			BEGIN
				SELECT @g_tx_err_msg = 'NEW Password cannot be same as OLD password!'
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (99999 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 99999
			END
		END

		SET	@tx_password = @tx_new_password

		IF (((@id_user_ver IS NULL) OR (@id_user_ver = -2147483648)))
		BEGIN
			SELECT	@id_user_ver	= USR.id_user_ver
			FROM	T_USER	USR
			WHERE	USR.id_user_key	= @id_user_key
		END

		SELECT  @tx_action_name = 'UPDATE'
	END
	ELSE
	BEGIN
		IF (@tx_password IS NULL) SET @tx_password = '?'
	END

	IF ((@tx_action_name = 'NEW'))
	BEGIN
		EXEC @g_id_return_status = GET_system_key @id_env_key	= @g_id_env_key, @tx_key_name = 'id_user_key', @id_key_value	= @id_user_key OUTPUT, @num_keys = 1
	
		SELECT @g_tx_err_msg = 'Error generating key for id_user_key'
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30003 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30003
		END

		EXEC @g_id_return_status = INS_user
									  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_user_key			= @id_user_key		OUTPUT
									, @id_user_ver			= @id_user_ver		OUTPUT
									, @id_region_key 		= @id_region_key
									, @id_group_key			= @id_group_key
									, @id_legal_entity_key	= @id_legal_entity_key
									, @tx_first_name		= @tx_first_name
									, @tx_last_name			= @tx_last_name
									, @tx_user_alias 		= @tx_user_alias
									, @tx_login_name		= @tx_login_name
									, @tx_password			= @tx_password
									, @is_disabled			= @is_disabled
									, @is_allow_login 		= @is_allow_login
									, @is_first_login 		= 1
									, @id_created_by_key	= @id_created_by_key
									, @tx_email				= @tx_email


		SELECT @g_tx_err_msg = 'Error calling SP -> [INS_user] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END
	END


	IF ((@tx_action_name = 'DELETE'))
	BEGIN
		SELECT  @tx_action_name = 'UPDATE', @is_active = 0
	END

	IF @tx_action_name IN ('UPDATE')
	BEGIN
		EXEC @g_id_return_status = UPD_user
									  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_user_key			= @id_user_key		OUTPUT
									, @id_user_ver			= @id_user_ver		OUTPUT
									, @id_region_key 		= @id_region_key
									, @id_group_key			= @id_group_key
									, @id_legal_entity_key 	= @id_legal_entity_key
									, @tx_first_name		= @tx_first_name
									, @tx_last_name			= @tx_last_name
									, @tx_user_alias 		= @tx_user_alias
									, @tx_login_name		= @tx_login_name
									, @tx_password			= @tx_password
									, @is_disabled			= @is_disabled
									, @is_allow_login 		= @is_allow_login
									, @is_first_login 		= @is_first_login
									, @id_created_by_key	= @id_created_by_key
									, @tx_email				= @tx_email

		SELECT @g_tx_err_msg = 'Error calling SP -> [UPD_user] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END
	END


	IF ( @tx_action_name IN ('MAP_USER_TO_GROUP', 'UNMAP_USER_TO_GROUP') )
	BEGIN
		IF ( ((@id_group_ver IS NULL) OR (@id_group_ver = -2147483648)) )
		BEGIN
			-- Get the latest version of UserGroup to map
			SELECT	@id_group_ver = id_group_ver
			FROM	T_GROUP
			WHERE	id_group_key = @id_group_key
			AND		is_active		= ISNULL(@is_active		, is_active)
				--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)

			SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
			SELECT @g_tx_err_msg = 'Error selecting data ' + ' from T_GROUP ' + '@id_group_key [' + CONVERT(varchar, @id_group_key) + '] '
			IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30006 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30006
			END
		END

		-- FIRST UPDATE USER with new version
		EXEC @g_id_return_status = UPD_user
									  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_user_key		= @id_user_key
									, @id_user_ver		= @id_user_ver
									, @tx_password		= @tx_password
									, @is_first_login 	= @is_first_login

		SELECT @g_tx_err_msg = 'Error calling SP -> [UPD_user] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END


		IF ((@tx_action_name = 'MAP_USER_TO_GROUP'))
		BEGIN
			SELECT @tx_action_name = 'MAP'
		END
		ELSE
		BEGIN
			SELECT @tx_action_name = 'UNMAP'
		END

		EXEC @g_id_return_status = ACT_generic_map
									  @tx_action_name		= @tx_action_name
										
											, @is_active			= @is_active
											, @id_env_key			= @id_env_key				OUTPUT
											, @id_user_mod_key		= @id_user_mod_key			OUTPUT
											, @dtt_mod				= @dtt_mod					OUTPUT
										
											, @id_state_key			= @id_state_key				OUTPUT
											, @tx_state_name		= @tx_state_name			OUTPUT
											, @id_action_key		= @id_action_key			OUTPUT
										
											, @id_event_key			= @id_event_key				OUTPUT
											, @id_event_map1_key	= @id_event_map1_key		OUTPUT
											, @id_event_map2_key	= @id_event_map2_key		OUTPUT
											, @id_event_map3_key	= @id_event_map3_key		OUTPUT
											, @id_event_map4_key	= @id_event_map4_key		OUTPUT
										
											, @dtt_valid_from		= @dtt_valid_from
											, @dtt_valid_to			= @dtt_valid_to
											, @dtt_as_at			= @dtt_as_at
											, @dtt_last_refresh		= @dtt_last_refresh
										
											, @is_sel_data			= @is_sel_data
											, @tx_log_level			= @tx_log_level
											, @id_log_level			= @id_log_level
											, @is_record_time		= @is_record_time
										 	, @is_print_msg  		= @is_print_msg
										 	, @is_persist_msg  		= @is_persist_msg
											, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_generic_map_key	= @id_generic_map_key		OUTPUT
									, @id_generic_map_ver	= @id_generic_map_ver		OUTPUT
									, @id_from_key			= @id_user_key
									, @id_from_key_ver		= @id_user_ver
									, @tx_from_type_name	= 'USER'
									, @id_to_key			= @id_group_key
									, @id_to_key_ver		= @id_group_ver
									, @tx_to_type_name		= 'GROUP'
									, @is_primary			= @is_primary
									, @ct_sort_order		= @ct_sort_order

		SELECT @g_tx_err_msg = 'Error calling SP -> [ACT_generic_map] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END
	END

	IF ((@tx_action_name = 'AUDIT_HISTORY'))
	BEGIN
		SELECT	tx_rs_type = 'RS_TYPE_AUDIT_HISTORY'
					, AUD.id_user_key
					, AUD.id_user_ver
					, AUD.id_user_mod_key
					, AUD.dtt_mod
					, AUD.dtt_mod
					, USR.tx_login_name
			FROM	T_USER_AUDIT	AUD
			JOIN	T_USER				USR		ON USR.id_user_key			= AUD.id_user_mod_key
			AND		AUD.id_user_key	=	ISNULL(@id_user_key	, AUD.id_user_key)
			AND		AUD.is_active		= ISNULL(@is_active		, AUD.is_active)
				--	AND		AUD.id_env_key	= ISNULL(@id_env_key	, AUD.id_env_key)
	END

	IF(@tx_action_name = 'SELECT_AT_LOGIN')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_USER'
				, USR.*		--Removed two fields [id_state_key and id_action_key, to resolve issue in middle tier]
				, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) AS tx_password_ref
				, LE.tx_legal_entity_id
				--, LE.tx_cbs_branch_id
				, LI.is_logged_in
				--, Li.tx_app_name
				, LI.tx_authentication_code
				, (SELECT TOP 1 tx_short_name FROM T_CLIENT WHERE is_active = 1) tx_client_name
		FROM		T_USER			USR
		JOIN		T_GROUP			GRP		ON	GRP.id_group_key		= USR.id_group_key
		LEFT JOIN 	T_LEGAL_ENTITY 	LE 		ON 	LE.id_legal_entity_key 	= USR.id_legal_entity_key
		LEFT JOIN 	T_LOGIN 		LI 		ON 	LI.id_user_key 			= USR.id_user_key
		WHERE	USR.id_user_key			= ISNULL(@id_user_key			, USR.id_user_key)
		AND		USR.id_group_key		= ISNULL(@id_group_key			, USR.id_group_key)
		AND		USR.tx_first_name		= ISNULL(@tx_first_name			, USR.tx_first_name)
		AND		USR.tx_last_name		= ISNULL(@tx_last_name			, USR.tx_last_name)
		AND		USR.tx_login_name		= ISNULL(@tx_login_name			, USR.tx_login_name)
		AND		CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) = ISNULL(@tx_password	, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)))
		AND		USR.tx_user_alias		= ISNULL(@tx_user_alias			, USR.tx_user_alias)
		AND		USR.is_active			= ISNULL(@is_active				, USR.is_active)
		AND		GRP.tx_group_name		= ISNULL(@tx_group_name			, GRP.tx_group_name)
		AND		GRP.is_active			= ISNULL(@is_active				, GRP.is_active)
		--AND		LI.tx_app_name			= ISNULL(@tx_app_name			, LI.tx_app_name)


		-- User Groups
		SELECT	tx_rs_type = 'RS_TYPE_GROUP', GRP.*
		FROM	V_USER_GROUP	GRP
		JOIN	T_USER			USR	ON	USR.id_user_key = GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
		

		-- Roles
		SELECT	tx_rs_type = 'RS_TYPE_ROLE', ROLE.*
		FROM	V_GROUP_ROLE		ROLE
		JOIN	V_USER_GROUP		GRP		ON GRP.id_group_key = ROLE.id_group_key
		JOIN	T_USER				USR		ON USR.id_user_key	= GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
		AND		ROLE.is_active		= ISNULL(@is_active		, ROLE.is_active)

		--- Swift mt

		SELECT	tx_rs_type = 'RS_TYPE_SWIFT_MODEL_MT_IN', GRP.*
		FROM	V_USER_SWIFT_MT_IN	GRP
		JOIN	T_USER			USR	ON	USR.id_user_key = GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)

		SELECT	tx_rs_type = 'RS_TYPE_SWIFT_MODEL_MT_OUT', GRP.*
		FROM	V_USER_SWIFT_MT_OUT	GRP
		JOIN	T_USER			USR	ON	USR.id_user_key = GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)

	END

	
	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_proc_end = GETDATE()
	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_tot_elapsed  = datediff(ss, @g_dtt_proc_start, @g_dtt_proc_end)

	SELECT @g_tx_err_msg = 'Exiting[ACT_user] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'ACT_user.sp', @g_id_line_num = 2241

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
	BEGIN
		-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'

		IF (0 = 0)
		BEGIN
			--RAISERROR (@tx_json_log_msg, 10, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10

		END
		ELSE
		BEGIN
			--RAISERROR (@tx_json_log_msg, 16, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
		END
	END

	RETURN 0
END
